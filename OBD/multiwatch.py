import obd
connection = obd.Async()
connection.watch(obd.commands.RPM)
connection.watch(obd.commands.FUEL_LEVEL)
connection.start()

while 1 == 1:
    speed = connection.query(obd.commands.SPEED)
    print(connection.query(obd.commands.RPM))
    print(connection.query(obd.commands.FUEL_LEVEL))


