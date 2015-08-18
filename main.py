	
from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4 import *
from client import Client
from chat import ChatDialog
import sys
import string
import main_ui
import json

class MainDialog(QDialog,main_ui.Ui_Dialog):
	def __init__(self,cli,parent=None):
		super(MainDialog,self).__init__(parent)
		mu=main_ui.Ui_Dialog()
		self.setupUi(self)
		self.client=cli
		self.init_friend_list_view()
	
	def init_friend_list_view(self):
		self.client.update_friend_list()
		friend_list=self.client.get_friend_list()
		self.friend_ui_arr=[]
		for i,friend in enumerate(friend_list):
			self.friend_listview.addItem(QtGui.QListWidgetItem(QtGui.QIcon("./111.png"),friend.nickname))
			self.friend_ui_arr.append(friend)
		
		print self.friend_ui_arr
		self.friend_listview.setContextMenuPolicy(QtCore.Qt.CustomContextMenu)
		self.friend_listview.customContextMenuRequested.connect(self.contextMenuRequested)
		self.friend_listview.doubleClicked.connect(self.friend_list_doubleClicked)
		self.friend_listview.clicked.connect(self.friend_list_clicked)
		
		
	def contextMenuRequested(self, point):
		item=self.friend_listview.itemAt(point)
		print 'item=',point
		#cur_item=self.friend_listview.currentRow()
		cur_item=self.friend_listview.currentItem()
		item_data=cur_item.text()
		cur_item.setForeground(QtGui.QBrush(Qt.blue))
		#cur_item.setBackground(QtGui.QBrush(Qt.blue))
		print 'cur_item=',item_data
		if item != None:
			self.rightMenuShow()
			
	def rightMenuShow(self):
		rightMenu =QtGui.QMenu(self.friend_listview)
		removeAction=QtGui.QAction(u'remove',self,triggered=self.close)
		rightMenu.addAction(removeAction)
		addAction=QtGui.QAction(u'add',self,triggered=self.additem)
		rightMenu.addAction(addAction)
		rightMenu.exec_(QtGui.QCursor.pos())
	def additem(self):
		print 'add item'
		pass
	def friend_list_doubleClicked(self):
		print 'friend list double clicked.........'
		cur_index=self.friend_listview.currentRow() 
		friend=self.friend_ui_arr[cur_index]
		self.chatDialog = ChatDialog(self.client,friend)
		self.chatDialog.show()
		
	def	friend_list_clicked(self):
		print 'friend list clicked.........'
		
	def	friend_list_onItem(self):
		print 'friend list on item.........'

