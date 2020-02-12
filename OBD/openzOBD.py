import obd
connection = obd.Async()
connection.watch(obd.commands.RPM)
connection.start()

while 1 == 1:
    print(connection.query(obd.commands.RPM))

