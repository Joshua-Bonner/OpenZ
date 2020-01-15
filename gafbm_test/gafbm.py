import pygame
import math
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
			
			if (square.get_avg() < self.avg_colorL):
				current_color = black
			else:
				current_color = white

			for i in range (square_xy[0], square_xy[0] + 10, 1):
				for j in range (square_xy[1], square_xy[1] + 10, 1):
					pic.putpixel((i,j), current_color)

		for square in self.bmR:
			square_xy = square.get_xy()
			
			if (square.get_avg() < self.avg_colorR):
				square.set_line(False)
				current_color = black
			else:
				square.set_line(True)
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

			(self.bmL_matrix[row]).append(square)
		
		row = 0
		last_xy = self.bmR[0].get_xy()
		for square in self.bmR:
			current_xy = square.get_xy()
			if (last_xy[1] < current_xy[1]):
				self.bmR_matrix.append([])
				row += 1

			(self.bmR_matrix[row]).append(square)				

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
	
	def has_line(self):
		return self.has_line

def main():
	picture = Image.open("testing2.png")
	screenSize = (800,480)

	picture = picture.resize(screenSize)
	picture = picture.convert('LA')
	picture = picture.filter(ImageFilter.GaussianBlur(radius = 3))
	picture = picture.convert('RGB')
	
	draw_gafbm(picture)

	picture.show()

def draw_gafbm(pic):
	matrix = box_matrix()

	drawer = ImageDraw.Draw(pic)

	#drawer.rectangle((10, 200, 110, 300), fill=None, outline=255)

	#drawer.rectangle((690, 200, 790, 300), fill=None, outline=255)
	
	for y in range (200, 291, 10):
		for x in range (10, 151, 10):
			matrix.add_box(box(x,y), pic, 0)

	for y in range (200, 291, 10):
		for x in range( 640, 781, 10):
			matrix.add_box(box(x,y), pic, 1)
	
	matrix.find_avg_color()
	pic = matrix.paint_lines(pic)
	
	del drawer

main()




