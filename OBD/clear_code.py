import obd
connection = obd.OBD()  # auto-connects to USB or RF port

cmd = obd.commands.CLEAR_DTC  # select an OBD command (sensor)

response = connection.query(cmd)
print(response)
