exec=client
CC=gcc
OBJS=client.o

all:$(exec)
	
$(exec):$(OBJS)
	$(CC) -o $@ $(OBJS) -lpthread
	
clean:
	rm *.o $(exec)
	
