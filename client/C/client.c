#include <stdio.h>
#include <pthread.h>
#include <netinet/in.h>  
#include <sys/types.h>   
#include <sys/socket.h>   
int cli_fd = -1;
void send_msg(void *arg) {
	while(1) {
	printf("send msg thread....\n");
	sleep(3);
	}
}
void rcv_msg(void *arg) {
	while(1) {
		printf("rcv msg thread....\n");
		sleep(3);
	}
}

int connect_to_server(char *ip,int port) {
	int fd = -1;
	struct sockaddr_in server_addr;
    fd = socket(AF_INET,SOCK_STREAM,0);
    if( fd < 0)
    {
        printf("Create Socket Failed!\n");
        return -1;
    }

    bzero(&server_addr,sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    if(inet_aton(ip,&server_addr.sin_addr) <= 0) 
    {
        printf("inet aton error!\n");
        return -1;
    }
    server_addr.sin_port = htons(port);
    if(connect(fd,(struct sockaddr*)&server_addr, sizeof(server_addr)) < 0)
    {
        printf("Can Not Connect To %s!\n",ip);
        return -1;
    }
	return fd;
}


int main(int argc,char *argv[]){
	printf("pychat c client......init.\n");
	pthread_t s_id,r_id;
	int res = 0;
	cli_fd = connect_to_server("192.168.17.134",1088);
	if ( cli_fd <= 0 ) 
		printf("connect to server error.\n");
	else {
		printf("connect to server success,fd = %d\n",cli_fd);
	}
	res = pthread_create(&s_id,NULL,(void *)&rcv_msg,NULL);
	if ( 0 != res) {
		printf("create rcv msg thread.....error.\n");
	}
	pthread_create(&r_id,NULL,(void *)&send_msg,NULL);
	if ( 0 != res) {
		printf("create send msg thread.....error.\n");
	}

	while(1) {
		printf(".........");
		sleep(10000);
	}
	return 0;
}
