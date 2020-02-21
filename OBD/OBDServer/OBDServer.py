import SocketServer
import time
import obd

connection = obd.Async()
connection.watch(obd.commands.RPM)
connection.watch(obd.commands.INTAKE_TEMP)
connection.watch(obd.commands.THROTTLE_POS)
connection.watch(GET_DTC)
connection.watch(CLEAR_DTC)
connection.watch(COOLANT_TEMP)

class OBDServer(SocketServer.BaseRequestHandler):
    HOST = "localhost"
    PORT = 6969

    def handle(self):
        time.sleep(0.01)
        self.data = self.request.recv(1024).strip()
        return_query = ""
        for query in self.data.split('/'):
            query_switch = {
                "RPM": obd.commands.RPM,
                "INTAKE_TEMP": obd.commands.INTAKE_TEMP,
                "THROTTLE_POS": obd.commands.THROTTLE_POS,
                "GET_DTC": obd.commands.GET_DTC,
                "CLEAR_DTC": obd.commands.CLEAR_DTC,
                "COOLANT_TEMP": obd.commands.COOLANT_TEMP
            }
            return_query += connection.query(query_switch.get(query)) + '/'

        print("{} wrote:".format(self.client_address[0]))
        self.request.sendall("Pong")
        print(self.data)



if __name__ == "__main__":
    server = SocketServer.TCPServer((OBDServer.HOST, OBDServer.PORT), OBDServer)
    server.serve_forever()
