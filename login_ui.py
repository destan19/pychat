# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'test.ui'
#
# Created: Thu Aug 13 02:34:15 2015
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
        Dialog.resize(394, 274)
        self.loginButton = QtGui.QPushButton(Dialog)
        self.loginButton.setGeometry(QtCore.QRect(120, 180, 161, 21))
        self.loginButton.setObjectName(_fromUtf8("loginButton"))
        self.username = QtGui.QLineEdit(Dialog)
        self.username.setGeometry(QtCore.QRect(122, 80, 171, 27))
        self.username.setObjectName(_fromUtf8("username"))
        self.password = QtGui.QLineEdit(Dialog)
        self.password.setGeometry(QtCore.QRect(122, 120, 171, 27))
        self.password.setObjectName(_fromUtf8("password"))
        self.label = QtGui.QLabel(Dialog)
        self.label.setGeometry(QtCore.QRect(30, 90, 66, 17))
        self.label.setObjectName(_fromUtf8("label"))
        self.label_2 = QtGui.QLabel(Dialog)
        self.label_2.setGeometry(QtCore.QRect(30, 120, 66, 17))
        self.label_2.setObjectName(_fromUtf8("label_2"))
        self.statusLabel = QtGui.QLabel(Dialog)
        self.statusLabel.setGeometry(QtCore.QRect(95, 220, 151, 41))
        self.statusLabel.setText(_fromUtf8(""))
        self.statusLabel.setObjectName(_fromUtf8("statusLabel"))
        self.retranslateUi(Dialog)
        QtCore.QMetaObject.connectSlotsByName(Dialog)

    def retranslateUi(self, Dialog):
        Dialog.setWindowTitle(_translate("Dialog", "Dialog", None))
        self.loginButton.setText(_translate("Dialog", "login", None))
        self.label.setText(_translate("Dialog", "username", None))
        self.label_2.setText(_translate("Dialog", "password", None))

