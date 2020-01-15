import pygame
from PIL import Image, ImageDraw
import sys

class box_matrix:
	def __init__(self):
		self.bm = []
		self.avg_color = 0
	
	def add_box(self, square, pic):
		square.set_avg(pic)
		self.bm.append(square)
	
	def find_avg_color(self):
		total_squares = 0
		for squares in self.bm:
			self.avg_color += squares.get_avg()
			total_squares += 1
		
		self.avg_color /= total_squares
	
	def paint_lines(self, pic):
		black = (0,0,0)
		white = (255,255,255)
		current_color = black
		for square in self.bm:
			square_xy = square.get_xy()
			
			if (square.get_avg() < self.avg_color):
				current_color = black
			else:
				current_color = white

			for i in range (square_xy[0], square_xy[0] + 10, 1):
				for j in range (square_xy[1], square_xy[1] + 10, 1):
					pic.putpixel((i,j), current_color)

		return pic
				

class box:
	def __init__(self, x, y):
		self.avg_color = 0
		self.x = x
		self.y = y
	
	def set_avg(self, picture):
		for i in range(self.x, self.x + 10, 1):
			for j in range(self.y, self.y + 10, 1):
				self.avg_color += picture.getpixel((i,j))[0]

		self.avg_color /= 100
	
	def get_avg(self):
		return self.avg_color
	
	def get_xy(self):
		return (self.x, self.y)

def main():
	picture = Image.open("testing.png")
	screenSize = (800,480)

	picture = picture.resize(screenSize)
	
	picture = picture.convert('LA')
	picture = picture.convert('RGB')
	
	draw_gafbm(picture)

	picture.show()

def draw_gafbm(pic):
	matrix = box_matrix()

	drawer = ImageDraw.Draw(pic)

	#drawer.rectangle((10, 200, 110, 300), fill=None, outline=255)

	#drawer.rectangle((690, 200, 790, 300), fill=None, outline=255)
	
	for x in range (10, 101, 10):
		for y in range (200, 291, 10):
			matrix.add_box(box(x,y), pic)

	for x in range (690, 781, 10):
		for y in range (200, 291, 10):
			matrix.add_box(box(x,y), pic)
	
	matrix.find_avg_color()
	pic = matrix.paint_lines(pic)

	del drawer


main()




