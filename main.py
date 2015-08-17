	
from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4 import *
from client import Client
import sys
import string
import main_ui
import json

class MainDialog(QDialog,main_ui.Ui_Dialog):
	def __init__(self,cli,parent=None):
		super(MainDialog,self).__init__(parent)
		mu=main_ui.Ui_Dialog()
		self.setupUi(self)
		client=cli
		friend_list=[]
		friend_list_json=client.friend_list_request()
	
		#for friend in friend_list_json['list']:
		#	friend_list.append(friend['nickname']
		for i,friend in enumerate(friend_list_json['list']):
			friend_list.append(friend['nickname'])
	
		print friend_list
		friend_list_model=listModel(friend_list)
		self.friend_listview.setModel(friend_list_model)

class listModel(QAbstractListModel): 
    def __init__(self, datain, parent=None, *args): 
        """ datain: a list where each item is a row
        """
        QAbstractListModel.__init__(self, parent, *args) 
        self.listdata = datain

    def rowCount(self, parent=QModelIndex()): 
        return len(self.listdata) 

    def data(self, index, role): 
        if index.isValid() and role == Qt.DisplayRole:
            return QVariant(self.listdata[index.row()])
        else: 
            return QVariant()


