import io
import threading
import math
import time
import picamera
from PIL import Image, ImageDraw, ImageFilter
import sys
from pydub import AudioSegment
from pydub.playback import play

night_threshold = 15
day_threshold = 5

threshold =  night_threshold

time_debug = False
matrix_debug = True
alert_debug = True

class box_matrix:
	def __init__(self):
		self.bmL = []
		self.bmR = []
		
		self.bmL_matrix = [[]]
		self.bmR_matrix =[[]]
		
		self.avg_colorL = 0
		self.avg_colorR = 0

		left_avg = 0
		right_avg = 0
		
		left_density = 0.0
		right_density = 0.0

		right_percent = 0.0
		left_percent = 0.0
	
	def add_box(self, square, pic, side):
		square.set_avg(pic)
		square_xy = square.get_xy()
		if (side == 0):
			self.bmL.append(square)
		else:
			self.bmR.append(square)
	
	def find_avg_color(self):
		total_squares = 0
		for squares in self.bmL:
			self.avg_colorL += squares.get_avg()
			total_squares += 1
		
		self.avg_colorL /= total_squares
		total_squares = 0
		
		for squares in self.bmR:
			self.avg_colorR += squares.get_avg()
			total_squares += 1

		self.avg_colorR /= total_squares

	
	def paint_lines(self, pic):
		black = (0,0,0)
		white = (255,255,255)

		left_total = 0
		right_total = 0
		count = 0
		
		current_color = black
		for square in self.bmL:
			square_xy = square.get_xy()
			
			if (square.get_avg() < (self.avg_colorL + threshold)):
				square.has_line = False
				current_color = black
			else:
				square.has_line = True
				left_total += 1
				current_color = white

			count += 1

			for i in range (square_xy[0], square_xy[0] + 5, 1):
				for j in range (square_xy[1], square_xy[1] + 5, 1):
					pic.putpixel((i,j), current_color)
		
		self.left_density = (1.0 * left_total) / count

		count = 0

		for square in self.bmR:
			square_xy = square.get_xy()
			
			if (square.get_avg() < (self.avg_colorR + threshold)):
				square.has_line = False
				current_color = black
			else:
				square.has_line = True
				right_total += 1
				current_color = white

			count += 1

			for i in range(square_xy[0], square_xy[0] + 5, 1):
				for j in range (square_xy[1], square_xy[1] + 5, 1):
					pic.putpixel((i,j), current_color)
		
		self.right_density = (1.0 * right_total) / count

		return pic
	
	def init_matrices(self):
		last_xy = self.bmL[0].get_xy()
		current_xy = (0,0)
		row = 0
	
		for square in self.bmL:
			current_xy = square.get_xy()
			if (last_xy[1] < current_xy[1]):
				self.bmL_matrix.append([])
				row += 1
			last_xy = square.get_xy()

			(self.bmL_matrix[row]).append(square)
		
		row = 0
		last_xy = self.bmR[0].get_xy()
		for square in self.bmR:
			current_xy = square.get_xy()
			if (last_xy[1] < current_xy[1]):
				self.bmR_matrix.append([])
				row += 1
			last_xy = square.get_xy()

			(self.bmR_matrix[row]).append(square)
	
	def out_matrix(self):
		print "LEFT MATRIX: "
		for row in self.bmL_matrix:
			for square in row:
				if (square.get_line()):
					print "#",
				else:
					print "_",
			print ""

		print "RIGHT MATRIX: "
		for row in self.bmR_matrix:
			for square in row:
				if (square.get_line()):
					print "#",
				else:
					print "_",
			print ""

	def calculate_distances(self, pic):
		rows = len(self.bmR_matrix)
		found = False;
		
		self.left_avg = 0
		self.right_avg = 0
		box_counter = 0
		num_detected = 0

		green = (0, 255, 0)
		
		for row in self.bmL_matrix:
			for square in reversed(row):
				if (square.get_line()):
					num_detected += 1
					self.left_avg += box_counter
					for i in range ((square.get_xy())[0], (square.get_xy())[0] + 5, 1):
						for j in range ((square.get_xy())[1], (square.get_xy())[1] + 5, 1):
							pic.putpixel((i,j), green)
					break
				box_counter += 1
			box_counter = 0
	
		self.left_avg /= rows
		self.left_percent = (num_detected * 1.0) / rows

		num_detected = 0
		box_counter = 0	
		
		for row in self.bmR_matrix:
			for square in (row):
				if (square.get_line()):
					self.right_avg += box_counter
					num_detected += 1
					for i in range ((square.get_xy())[0], (square.get_xy())[0] + 5, 1):
						for j in range ((square.get_xy())[1], (square.get_xy())[1] + 5, 1):
							pic.putpixel((i,j), green)
					break
				box_counter += 1
			box_counter = 0
		
		self.right_avg /= rows
		self.right_percent = (num_detected * 1.0) / rows

		return pic
				

class box:
	def __init__(self, x, y):
		self.avg_color = 0
		self.has_line = False;
		self.x = x
		self.y = y
	
	def set_avg(self, picture):
		total_pix = 0
		for i in range(self.x, self.x + 5, 1):
			for j in range(self.y, self.y + 5, 1):
				self.avg_color += picture.getpixel((i,j))[0]
				total_pix += 1

		self.avg_color /= total_pix
	
	def get_avg(self):
		return self.avg_color
	
	def get_xy(self):
		return (self.x, self.y)
	
	def set_line(self, line):
		self.has_line = line
	
	def get_line(self):
		return self.has_line

def main():
	frame_time = 0
	screenSize = (800,480)
	
	threadExists = False

	if (time_debug):
		start_time = time.time()

	with picamera.PiCamera() as camera:
		camera.resolution = screenSize
		camera.start_preview()
		camera.shutter_speed = 100
		camera.framerate = 30
		time.sleep(2)
	
	if (time_debug):
		print "Init Time: ", (time.time() - start_time)

	while True:
		stream = io.BytesIO()
		start_time = time.time()
		with picamera.PiCamera(resolution=screenSize, framerate=35) as camera:
			for foo in camera.capture_continuous(stream, format='jpeg', use_video_port=True):
				stream.truncate()
				stream.seek(0)
				break

		#thread = threading.Thread(target=on_thread, args=(stream,))
		#thread.start()
		picture = Image.open(stream)
		picture = picture.convert('LA')
		picture = picture.convert('RGB')
		in_lines = draw_gafbm(picture)
		if (in_lines):
			if threadExists:
				thread.join()
				threadExists = False
			thread = threading.Thread(target=on_thread)
			thread.start()
			threadExists = True


def on_thread():
	# REMOVED MULTITHREADED CALCULATIONS
	#screenSize = (800,480)
	#picture = Image.open(stream)
	#picture = picture.convert('LA')
	# Comment this out / remove this in the end
	#icture = picture.convert('RGB')
	#in_lines = draw_gafbm(picture)
	
	#if (in_lines):
	boop = AudioSegment.from_wav("boop.wav")
	play(boop)

def draw_gafbm(pic):
	matrix = box_matrix()
	matrix_length = 32
	
	if (time_debug):
		start_time = time.time()

	#left box
	for y in range (240, 291, 5):
		for x in range (150, 311, 5):
			matrix.add_box(box(x,y), pic, 0)
	#right box
	for y in range (240, 291, 5):
		for x in range( 485, 646, 5):
			matrix.add_box(box(x,y), pic, 1)
	
	if (time_debug):
		print "Init Matrix Time: ", (time.time() - start_time)
	
	if (time_debug):
		start_time = time.time()

	matrix.find_avg_color()
	pic = matrix.paint_lines(pic)

	if (time_debug): 
		print "Get average Color of Matrix and paint matrix Time: ", (time.time() - start_time)
	
	if (time_debug): 
		start_time = time.time()

	matrix.init_matrices()
	pic = matrix.calculate_distances(pic)

	if (time_debug):
		print "Build Matrix and Calculate Distances: ", (time.time() - start_time)

	if (alert_debug):
		if (matrix.left_percent < 0.5) and (matrix.right_percent < 0.5):
			print "No Lanes Detected - No Signal"
		elif (matrix.left_percent > 0.5) and (matrix.right_percent < 0.5):
			print "Left Lane Detected, Signal: ", (matrix.left_avg in range(4, (matrix_length / 4) + 8)),
			print "Lane Position: ", matrix.left_avg, " Acceptiable Range: 4 -> ", (matrix_length / 4) + 8
		elif (matrix.left_percent < 0.5) and (matrix.right_percent > 0.5):
			print "Right Lane Detected, Signal: ", (matrix.right_avg in range(4 , (matrix_length / 4) + 8)),
			print "Lane Position: ", matrix.right_avg, " Acceptiable Range: 4 -> ", (matrix_length / 4) + 8
		else:
			print "Both Lanes Detected, Signal: ", (matrix.left_avg in range(matrix.right_avg - 9, matrix.right_avg + 9)),
			print "Left Lane Position: ", matrix.left_avg, " Acceptiable Range: ", matrix.right_avg - 9, " -> " matrix.right_avg + 9
	
	if (matrix_debug):
		matrix.out_matrix()
			
	if (matrix.left_percent < 0.5) and (matrix.right_percent < 0.5):
		return True
	elif (matrix.left_percent > 0.5) and (matrix.right_percent < 0.5):
		return (matrix.left_avg in range(4, (matrix_length / 4) + 8))
	elif (matrix.left_percent < 0.5) and (matrix.right_percent > 0.5):
		return (matrix.right_avg in range(4, (matrix_length / 4) + 8))
	else:
		return (matrix.left_avg in range(matrix.right_avg - 9, matrix.right_avg + 9))
	
	
main()




