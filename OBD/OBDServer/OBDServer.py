import SocketServer
import time
import obd

connection = obd.Async()
connection.watch(obd.commands['ENGINE_LOAD'])
connection.watch(obd.commands['INTAKE_TEMP'])
connection.watch(obd.commands['INTAKE_PRESSURE'])
connection.start()


class OBDServer(SocketServer.BaseRequestHandler):
    HOST = "localhost"
    PORT = 6969

    def handle(self):
        time.sleep(0.01)
        self.data = self.request.recv(1024).strip()
        response = ""
        for query in self.data.split('/'):
            if query == 'CLEAR_DTC':
                connection.stop()
                connection.watch(obd.commands.CLEAR_DTC)
                connection.start()
                connection.query(obd.commands.CLEAR_DTC)
                connection.stop()
                connection.unwatch(obd.commands.CLEAR_DTC)
                connection.start()
            elif query == 'GET_DTC':
                connection.stop()
                connection.watch(obd.commands.GET_DTC)
                connection.start()
                connection.query(obd.commands.GET_DTC)
                connection.stop()
                connection.unwatch(obd.commands.GET_DTC)
                connection.start()

            #return_query = connection.query(query_switch.get(query))
            return_query = (connection.query(obd.commands[query]))
            response += str(return_query.value) + '/'
            print(return_query.value)

        #print("{} wrote:".format(self.client_address[0]))
        self.request.sendall(response)
        #print(self.data)


if __name__ == "__main__":
    server = SocketServer.TCPServer((OBDServer.HOST, OBDServer.PORT), OBDServer)
    server.serve_forever()
