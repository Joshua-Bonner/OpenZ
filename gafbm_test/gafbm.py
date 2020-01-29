import io
import pygame
import math
import time
import picamera
from PIL import Image, ImageDraw, ImageFilter
import sys

class box_matrix:
	def __init__(self):
		self.bmL = []
		self.bmR = []
		
		self.bmL_matrix = [[]]
		self.bmR_matrix =[[]]
		
		self.avg_colorL = 0
		self.avg_colorR = 0

		top_left = 0
		bottom_left = 0

		top_right = 0
		bottom_right = 0
	
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
		current_color = black
		for square in self.bmL:
			square_xy = square.get_xy()
			
			if (square.get_avg() < (self.avg_colorL + 5)):
				square.has_line = False
				current_color = black
			else:
				square.has_line = True
				current_color = white

			for i in range (square_xy[0], square_xy[0] + 10, 1):
				for j in range (square_xy[1], square_xy[1] + 10, 1):
					pic.putpixel((i,j), current_color)

		for square in self.bmR:
			square_xy = square.get_xy()
			
			if (square.get_avg() < (self.avg_colorR + 5)):
				square.has_line = False
				current_color = black
			else:
				square.has_line = True
				current_color = white

			for i in range(square_xy[0], square_xy[0] + 10, 1):
				for j in range (square_xy[1], square_xy[1] + 10, 1):
					pic.putpixel((i,j), current_color)

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
	
	def calculate_distances(self, pic):
		rows = len(self.bmR_matrix)
		top_row = 1
		bottom_row = rows - 1
		found = False;
		
		self.top_left = 0
		self.top_right = 0
		self.bottom_left = 0
		self.bottom_right = 0
		box_counter = 0

		green = (0, 255, 0)
		
		for square in reversed(self.bmL_matrix[top_row]):
			if (square.get_line()):
				self.top_left = box_counter
				for i in range ((square.get_xy())[0], (square.get_xy())[0] + 10, 1):
					for j in range ((square.get_xy())[1], (square.get_xy())[1] + 10, 1):
						pic.putpixel((i,j), green)
				break
			box_counter += 1

		box_counter = 0
		
		for square in reversed(self.bmL_matrix[bottom_row]):
			if (square.get_line()):
				self.bottom_left = box_counter
				for i in range ((square.get_xy())[0], (square.get_xy())[0] + 10, 1):
					for j in range ((square.get_xy())[1], (square.get_xy())[1] + 10, 1):
						pic.putpixel((i,j), green)
				break
			box_counter += 1

		box_counter = 0	
		
		for square in (self.bmR_matrix[top_row]):
			if (square.get_line()):
				self.top_right = box_counter
				for i in range ((square.get_xy())[0], (square.get_xy())[0] + 10, 1):
					for j in range ((square.get_xy())[1], (square.get_xy())[1] + 10, 1):
						pic.putpixel((i,j), green)
				break
			box_counter += 1

		box_counter = 0
		
		for square in (self.bmR_matrix[bottom_row]):
			if (square.get_line()):
				self.bottom_right = box_counter
				for i in range ((square.get_xy())[0], (square.get_xy())[0] + 10, 1):
					for j in range ((square.get_xy())[1], (square.get_xy())[1] + 10, 1):
						pic.putpixel((i,j), green)
				break
			box_counter += 1

		box_counter = 0


		return pic
				

class box:
	def __init__(self, x, y):
		self.avg_color = 0
		self.has_line = False;
		self.x = x
		self.y = y
	
	def set_avg(self, picture):
		total_pix = 0
		for i in range(self.x, self.x + 10, 1):
			for j in range(self.y, self.y + 10, 1):
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
	screenSize = (800,480)
	pygame.init()
	#picture = Image.open("testing.png")
	screen = pygame.display.set_mode(screenSize, pygame.HWSURFACE)
	
	start_time = time.time()

	with picamera.PiCamera() as camera:
		camera.resolution = screenSize
		camera.start_preview()
		camera.shutter_speed = 1000
		camera.framerate = 30
		time.sleep(2)
	
	print "Init Time: ", (time.time() - start_time)

	while True:
		stream = io.BytesIO()
		start_time = time.time()
		with picamera.PiCamera(resolution=screenSize, framerate=30) as camera:
			for foo in camera.capture_continuous(stream, format='jpeg', use_video_port=True):
				stream.truncate()
				stream.seek(0)
				break
		
		print "Time to take pic: ", (time.time() - start_time)
		start_time = time.time()
		
		picture = Image.open(stream)
		picture = picture.resize(screenSize)
		picture = picture.convert('LA')
		#picture = picture.filter(ImageFilter.GaussianBlur(radius = 3))
		picture = picture.convert('RGB')
		
		print "Time to process image in PIL: ", (time.time() - start_time)
		start_time = time.time()

		in_lines = draw_gafbm(picture)
		
		print "Time to algorithm on picture: ", (time.time() - start_time)
		start_time = time.time()

		pic_str = picture.tobytes("raw", 'RGB')
		pygame_surface = pygame.image.fromstring(pic_str, screenSize, picture.mode)
		screen.blit(pygame_surface, (0,0))
		
		if (in_lines):
			pygame.draw.line(screen, (255,0,0), (0,0), (800,480))
			pygame.draw.line(screen, (255,0,0), (800,0), (0, 480)) 

		pygame.display.flip()

		print "Time to draw pic on pygame: ", (time.time() - start_time)


def draw_gafbm(pic):
	matrix = box_matrix()

	for y in range (220, 271, 10):
		for x in range (175, 276, 10):
			matrix.add_box(box(x,y), pic, 0)

	for y in range (220, 271, 10):
		for x in range( 505, 606, 10):
			matrix.add_box(box(x,y), pic, 1)
	
	matrix.find_avg_color()
	pic = matrix.paint_lines(pic)
	matrix.init_matrices()
	pic = matrix.calculate_distances(pic)

	print "TOP LEFT: ", matrix.top_left
	print "BOTTOM LEFT: ", matrix.bottom_left
	print "TOP RIGHT: ", matrix.top_right
	print "BOTTOM RIGHT: ", matrix.bottom_right
	
	avgL = (matrix.top_left + matrix.bottom_left) / 2
	avgR = (matrix.top_right + matrix.bottom_right) / 2

	return (avgL in range(avgR - 1, avgR + 1))
	
	
main()




