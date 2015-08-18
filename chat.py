	
from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4 import *

import sys
import string
import chat_ui
import json
from friend import Friend

class ChatDialog(QDialog,chat_ui.Ui_Dialog):
	def __init__(self,client,friend,parent=None):
		super(ChatDialog,self).__init__(parent)
		mu=chat_ui.Ui_Dialog()
		self.setupUi(self)
		self.client = client
		self.friend = friend
		print 'chat with friend ',friend.nickname
		


