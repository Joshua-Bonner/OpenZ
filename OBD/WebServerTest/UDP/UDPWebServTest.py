import random
import socket

server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_socket.bind(('', 6969))

while True:
    rand = random.randint(0, 10)
    message, address = server_socket.recvfrom(6144)
    print(message)
    message = "https://www.youtube.com/watch?v=IIeSGUK-Lyo"
    if rand >= 4:
        server_socket.sendto(message, address)
