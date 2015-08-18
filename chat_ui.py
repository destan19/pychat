# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'chat.ui'
#
# Created: Mon Aug 17 19:39:54 2015
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
        Dialog.resize(557, 387)
        self.msg_text_edit = QtGui.QTextEdit(Dialog)
        self.msg_text_edit.setGeometry(QtCore.QRect(-20, 0, 461, 211))
        self.msg_text_edit.setObjectName(_fromUtf8("msg_text_edit"))
        self.send_text_edit = QtGui.QTextEdit(Dialog)
        self.send_text_edit.setGeometry(QtCore.QRect(0, 250, 441, 91))
        self.send_text_edit.setObjectName(_fromUtf8("send_text_edit"))
        self.verticalScrollBar = QtGui.QScrollBar(Dialog)
        self.verticalScrollBar.setGeometry(QtCore.QRect(420, 0, 16, 201))
        self.verticalScrollBar.setOrientation(QtCore.Qt.Vertical)
        self.verticalScrollBar.setObjectName(_fromUtf8("verticalScrollBar"))
        self.send_push_button = QtGui.QPushButton(Dialog)
        self.send_push_button.setGeometry(QtCore.QRect(320, 350, 98, 27))
        self.send_push_button.setObjectName(_fromUtf8("send_push_button"))
        self.msg_text_edit_2 = QtGui.QTextEdit(Dialog)
        self.msg_text_edit_2.setGeometry(QtCore.QRect(-10, 0, 461, 211))
        self.msg_text_edit_2.setObjectName(_fromUtf8("msg_text_edit_2"))

        self.retranslateUi(Dialog)
        QtCore.QMetaObject.connectSlotsByName(Dialog)

    def retranslateUi(self, Dialog):
        Dialog.setWindowTitle(_translate("Dialog", "Dialog", None))
        self.send_push_button.setText(_translate("Dialog", "send", None))

