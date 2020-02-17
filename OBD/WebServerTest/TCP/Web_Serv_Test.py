import SocketServer
import time


class OBDServer(SocketServer.BaseRequestHandler):
    HOST = "localhost"
    PORT = 6969

    def handle(self):
        time.sleep(0.01)
        self.data = self.request.recv(1024).strip()
        print("{} wrote:".format(self.client_address[0]))
        self.request.sendall("Pong")
        print(self.data)
        # just send back the same data, but upper-cased
        #self.request.sendall("Hello World")


if __name__ == "__main__":
    server = SocketServer.TCPServer((OBDServer.HOST, OBDServer.PORT), OBDServer)
    server.serve_forever()
