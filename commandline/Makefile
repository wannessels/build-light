# Name: Makefile
# Project: hid-custom-rq example
# Author: Christian Starkjohann
# Creation Date: 2008-04-06
# Tabsize: 4
# Copyright: (c) 2008 by OBJECTIVE DEVELOPMENT Software GmbH
# License: GNU GPL v2 (see License.txt), GNU GPL v3 or proprietary (CommercialLicense.txt)
# This Revision: $Id: Makefile 692 2008-11-07 15:07:40Z cs $


# Concigure the following definitions according to your system.
# This Makefile has been tested on Mac OS X, Linux and Windows.

# Use the following 3 lines on Unix (uncomment the framework on Mac OS X):
#USBFLAGS = `libusb-config --cflags`
#USBLIBS = `libusb-config --libs`
#EXE_SUFFIX =

# Use the following 3 lines on Windows and comment out the 3 above. You may
# have to change the include paths to where you installed libusb-win32
#USBFLAGS = -I/usr/local/include
#USBLIBS = -L/usr/local/lib -lusb
#EXE_SUFFIX = .exe
USBFLAGS=-IC:\avr\libusb-win32-bin-1.2.1.0\include
USBLIBS=-LC:\MinGW\lib\ -LC:\avr\libusb-win32-bin-1.2.1.0\lib\gcc C:\avr\libusb-win32-bin-1.2.1.0\lib\gcc\libusb.a
EXE_SUFFIX=.exe

NAME = set-led

OBJECTS = opendevice.o $(NAME).o

CC		= gcc
CFLAGS	= $(CPPFLAGS) $(USBFLAGS) -O -g -Wall
LIBS	= $(USBLIBS)

PROGRAM = $(NAME)$(EXE_SUFFIX)


all: $(PROGRAM)

.c.o:
	$(CC) $(CFLAGS) -c $<

$(PROGRAM): $(OBJECTS)
	$(CC) -o $(PROGRAM) $(OBJECTS) $(LIBS)

strip: $(PROGRAM)
	strip $(PROGRAM)

clean:
	rm -f *.o $(PROGRAM)
