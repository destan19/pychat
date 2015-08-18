# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'main.ui'
#
# Created: Tue Aug 18 06:39:02 2015
#      by: PyQt4 UI code generator 4.10.4
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui

try:
    _fromUtf8 = QtCore.QString.fromUtf8
except AttributeError:
    def _fromUtf8(s):
        return s

try:
    _encoding = QtGui.QApplication.UnicodeUTF8
    def _translate(context, text, disambig):
        return QtGui.QApplication.translate(context, text, disambig, _encoding)
except AttributeError:
    def _translate(context, text, disambig):
        return QtGui.QApplication.translate(context, text, disambig)

class Ui_Dialog(object):
    def setupUi(self, Dialog):
        Dialog.setObjectName(_fromUtf8("Dialog"))
        Dialog.resize(251, 525)
        self.friend_listview = QtGui.QListWidget(Dialog)
        self.friend_listview.setGeometry(QtCore.QRect(0, 80, 251, 391))
        self.friend_listview.setObjectName(_fromUtf8("friend_listview"))
        self.nickname_label = QtGui.QLabel(Dialog)
        self.nickname_label.setGeometry(QtCore.QRect(110, 10, 141, 21))
        self.nickname_label.setText(_fromUtf8(""))
        self.nickname_label.setObjectName(_fromUtf8("nickname_label"))
        self.sig_label = QtGui.QLabel(Dialog)
        self.sig_label.setGeometry(QtCore.QRect(110, 40, 141, 21))
        self.sig_label.setText(_fromUtf8(""))
        self.sig_label.setObjectName(_fromUtf8("sig_label"))
        self.user_image_label = QtGui.QLabel(Dialog)
        self.user_image_label.setGeometry(QtCore.QRect(0, 10, 81, 61))
        self.user_image_label.setText(_fromUtf8(""))
        self.user_image_label.setObjectName(_fromUtf8("user_image_label"))

        self.retranslateUi(Dialog)
        QtCore.QMetaObject.connectSlotsByName(Dialog)

    def retranslateUi(self, Dialog):
        Dialog.setWindowTitle(_translate("Dialog", "Dialog", None))

