	
from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4 import *
from client import Client
import sys
import string
import login_ui
from main import MainDialog
class TestDialog(QDialog,login_ui.Ui_Dialog):
	def __init__(self,parent=None):
		super(TestDialog,self).__init__(parent)
		firstui=login_ui.Ui_Dialog()
		self.setupUi(self)
		self.password.setEchoMode(QtGui.QLineEdit.Password)
		self.loginButton.clicked.connect(self.buttonPressed)
	def buttonPressed(self):
		username_text=self.username.text()
		password_text=self.password.text()
		username=unicode(username_text).encode("utf-8")
		password=unicode(password_text).encode("utf-8")
		self.client=Client(string.atoi(username),string.atoi(password))		
		self.client.connect_to_server("192.168.17.134",80)
		res=self.client.login()
		print res
		if res == 1:
			self.statusLabel.setText("login success")
			self.accept();
			self.mDialog=MainDialog(self.client)
			self.mDialog.show()
		else:
			self.statusLabel.setText("username or password error")
			

app=QApplication(sys.argv)

dialog=TestDialog()
dialog.show()
#app.exec_()
app.exec_()