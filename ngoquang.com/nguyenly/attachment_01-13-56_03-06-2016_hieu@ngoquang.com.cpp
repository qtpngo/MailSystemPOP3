#include <stdio.h>
#include <stdlib.h>
#include <windows.h>
#define MAX 10
int allocation[MAX][MAX], available[MAX], maxClaim[MAX][MAX], need[MAX][MAX],
allocated[MAX], maxResource[MAX], running[MAX], order[MAX], count = 0, numOfResources, numOfProcesses, selection, indexOfOrder = 0;
bool wrongSelection = true, checkLoop = true;

void loadDataOfProcesses(char* fileName){
	
	printf("File name is: %s\n", fileName);
	FILE *file;
	file = fopen(fileName, "r");
	
	if (file == NULL){
		printf("File not found!");
	} else {
		printf("Reading file...\n");
		
		fscanf(file, "Number Of Processes: %d\n", &numOfProcesses);
	
		fscanf(file, "Number Of Resources: %d\n", &numOfResources);
		
		fscanf(file, "Allocation Matrix:\n");
		for(int i = 0; i < numOfProcesses; i++){
			for (int j = 0; j < numOfResources; j++){
				fscanf(file, (j == numOfResources - 1) ? "%d\n" : "%d ", &allocation[i][j]);
			}
		}
		
		fscanf(file, "Max Matrix:\n");	
		for(int i = 0; i < numOfProcesses; i++){
			for (int j = 0; j < numOfResources; j++){
				fscanf(file, (j == numOfResources - 1) ? "%d\n" : "%d ", &maxClaim[i][j]);
			}
		}
		
		fscanf(file, "Available Vector:\n");
		for (int i = 0; i < numOfResources; i++){
			fscanf(file, (i == numOfResources - 1) ? "%d\n" : "%d ", &available[i]);
		}
		
		printf("File reading finished.\n");
	}
	
	fclose(file);	
}

void enterDataOfResourcesByKeyboard(){
	printf("Enter the number of processes: ");
	scanf("%d", &numOfProcesses);
	
	printf("\nEnter the number of resources: ");
	scanf("%d", &numOfResources);
	
	printf("\nEnter Allocation matrix: \n");   
	for (int i = 0; i < numOfProcesses; i++){
		for (int j = 0; j < numOfResources; j++){
			scanf("%d", &allocation[i][j]);
		}
	}
	
	printf("\nEnter Max Claim matrix: \n");
	for (int i = 0; i < numOfProcesses; i++){
		for (int j = 0; j < numOfResources; j++){
			scanf("%d", &maxClaim[i][j]);
		}
	}
	
	printf("\nEnter Available vector: \n");
	for (int i = 0; i < numOfResources; i++){
		scanf("%d", &available[i]);
	}
	
	printf("Finished enter by keyboard.");
	system("PAUSE");
}

void calculateAllocatedVector(){
	for (int i = 0; i < numOfResources; i++){
		allocated[i] = 0;
		for (int j = 0; j < numOfProcesses; j++){
			allocated[i] += allocation[j][i];
		}
	}
}

void calculateMaxResourceVector(){
	for (int i = 0; i < numOfResources; i++){
		maxResource[i] = available[i] + allocated[i];
	}
}

void calculateNeedMatrix(){
	for (int i = 0; i < numOfProcesses; i++){
		for (int j = 0; j < numOfResources; j++){
			need[i][j] = maxClaim[i][j] - allocation[i][j];
		}
	}
}

void setCountAndRunningState(){
	for (int i = 0; i < numOfProcesses; i++){
		running[i] = 1;
		count++;
	}
}

void printAllData(){
	printf("The Allocation matrix is: \n");
	for (int i = 0; i < numOfProcesses; i++){
		for (int j = 0; j < numOfResources; j++){
			printf("%3d", allocation[i][j]);
		}
		printf("\n");
	}
	
	printf("The Max Claim matrix is: \n");
	for (int i = 0; i < numOfProcesses; i++){
		for (int j = 0; j < numOfResources; j++){
			printf("%3d", maxClaim[i][j]);
		}
		printf("\n");
	}
	
	printf("The Need matrix is: \n");
	for (int i = 0; i < numOfProcesses; i++){
		for (int j = 0; j < numOfResources; j++){
			printf("%3d", need[i][j]);
		}
		printf("\n");
	}
	
	printf("\nThe Available vector is: ");
	for (int i = 0; i < numOfResources; i++){
		printf("%3d", available[i]);
	}
	
	printf("\nThe Max Resource vector is: ");
	for (int i = 0; i < numOfResources; i++){
		printf("%3d", maxResource[i]);
	}
	printf("\n");
}

void checkIfSystemIsSafe(){
	system("cls");
	int t_allocation[numOfProcesses][numOfResources], t_available[numOfProcesses], t_maxClaim[numOfProcesses][numOfResources], t_need[numOfProcesses][numOfResources],
		t_allocated[numOfProcesses], t_maxResource[numOfProcesses], t_running[numOfProcesses], t_order[numOfProcesses], t_count, t_execute, t_safe, t_indexOfOrder = 0;
	for (int i = 0; i < numOfProcesses; i++){
		t_available[i] = available[i];
		t_allocated[i] = allocated[i];
		t_maxResource[i] = maxResource[i];
		t_running[i] = running[i];
		for (int j = 0; j < numOfResources; j++){
			t_allocation[i][j] = allocation[i][j];
			t_maxClaim[i][j] = maxClaim[i][j];
			t_need[i][j] = need[i][j];
		}
	}
	t_count = count;
	FILE *file;
	file = fopen("Output.txt", "w");
	char* space = (char*) " ";
	fprintf(file, "OUTPUT");
	while (t_count != 0){
		int space_allocation = 20;
		int processIsProcessing = 0;
		t_safe = 0;
		for (int i = 0; i < numOfProcesses; i++){
			if (t_running[i]){
				t_execute = 1;
				for (int j = 0; j < numOfResources; j++){
					if (t_need[i][j] > t_available[j]){
						t_execute = 0;
						break;
					}
				}
				if (t_execute){
					processIsProcessing = i;
//					printf("\nProcess %d is executing.", i);
					fprintf(file, "\nProcess %d is executing.", i);
					t_order[t_indexOfOrder++] = i;
					t_running[i] = 0;
					t_count--;
					t_safe = 1;
						
					for (int k = 0; k < numOfResources; k++){
						t_available[k] += t_allocation[i][k];
					}
					break;
					}
				}
			}
		if (!t_safe) {
			printf("\nThe processes are in unsafe state.\n");
			fprintf(file, "\nThe processes are in unsafe state.");
			return ;
		} else {
			printf("%7sAllocation%10sMax%17s\n", space, space, space);
			for (int i = 0; i < numOfProcesses; i++){
				int space_allocation = 20;
				printf("P%d   ", i);
				for (int j = 0; j < numOfResources; j++){
					printf("%3d", t_allocation[i][j]);
				}
				space_allocation -= numOfResources * 3;
				for (int k = 0; k < space_allocation; k++){
					printf(" ");
				}
				for (int j = 0; j < numOfResources; j++){
					printf("%3d", t_maxClaim[i][j]);
				}
				for (int k = 0; k < space_allocation; k++){
					printf(" ");
				}
				if (i == processIsProcessing){
					printf("(This process is executing.)");
				}
				printf("\n");
			}
			printf("Available: ");
			for (int i = 0; i < numOfResources; i++){
				printf("%3d", t_available[i]);
			}
			Sleep(1000);
			if (t_count != 0) system("cls");
//			printf("\nThe process is in safe state.");
//			printf("\nAvailable vector: ");
//					
//			for (int i = 0; i < numOfResources; i++){
//				printf("%3d", t_available[i]);
//			}
//			
			fprintf(file, "\nThe process is in safe state.");
			fprintf(file, "\nAvailable vector: ");
					
			for (int i = 0; i < numOfResources; i++){
				fprintf(file, "%3d", t_available[i]);
			}
		}
	}
	printf("\n\nThe safe order to execute is: ");
	for (int i = 0; i < t_indexOfOrder; i++){
		printf("%3d", t_order[i]);
	}
	printf("\n");
	
	fprintf(file, "\n\nThe safe order to execute is: ");
	for (int i = 0; i < t_indexOfOrder; i++){
		fprintf(file, "%3d", t_order[i]);
	}
}

void checkRequest(){
	int request[numOfResources], processNumber;
	while (true){
		printf("Enter your process number which requests some resources: ");
		scanf("%d", &processNumber);
		if (processNumber >= 0 && processNumber < numOfProcesses){
			break;
		}
	}
	printf("Enter your request vector: ");
	for (int i = 0; i < numOfResources; i++){
		scanf("%d", &request[i]);
	}
	
	for (int i = 0; i < numOfResources; i++){
		if (request[i] > need[processNumber][i]) {
			printf("No free resources!\n");
			system("PAUSE");
			return ;
		}
		if (request[i] > available[i]){
			printf("No available resources can be used!\n");
			system("PAUSE");
			return ;
		}
	}
	for (int i = 0; i < numOfResources; i++){
		allocation[processNumber][i] += request[i];
		need[processNumber][i] -= request[i];
		available[i] -= request[i];
	}
	
	printf("Your request is accepted.\nThe new allocation of process %d is\nP%3d", processNumber, processNumber);
	for (int i = 0; i < numOfResources; i++){
		printf("%3d", allocation[processNumber][i]);
	}
	fflush(stdin);
	printf("\nContinue to check if system? (Y/N)");
	char c;
	scanf("%c", &c);
	if (c == 'Y' || c == 'y'){
		printf("Now move to check system...\n");
		Sleep(1000);
		checkIfSystemIsSafe();
		system("PAUSE");
	} else {
		printf("Back to main program...\n");
		Sleep(1000);
		return ;
	}
}

void writeDataToFile(){
	FILE *file;
	file = fopen("Output.txt", "w");
	
	fprintf(file, "Number Of Processes: %d\n", numOfProcesses);
	
	fprintf(file, "Number Of Resources: %d\n", numOfResources);
		
	fprintf(file, "Allocation Matrix:\n");
	for(int i = 0; i < numOfProcesses; i++){
		for (int j = 0; j < numOfResources; j++){
			fprintf(file, (j == numOfResources - 1) ? "%d\n" : "%d ", allocation[i][j]);
		}
	}
		
	fprintf(file, "Max Matrix:\n");	
	for(int i = 0; i < numOfProcesses; i++){
		for (int j = 0; j < numOfResources; j++){
			fprintf(file, (j == numOfResources - 1) ? "%d\n" : "%d ", maxClaim[i][j]);
		}
	}
		
	fprintf(file, "Available Vector:\n");
	for (int i = 0; i < numOfResources; i++){
		fprintf(file, (i == numOfResources - 1) ? "%d\n" : "%d ", available[i]);
	}
		
	printf("File writing finished.\n");
}

int main(){
	int loadDataFromFileOrEnterByKeyboard;
	
	while (true) {
		printf("Enter 1 to load data from file, 2 for enter by keyboard: ");
		scanf("%d", &loadDataFromFileOrEnterByKeyboard);
		if (loadDataFromFileOrEnterByKeyboard == 1){
			loadDataOfProcesses((char*)"Resources3.txt");
			break;
		} else if (loadDataFromFileOrEnterByKeyboard == 2){
			enterDataOfResourcesByKeyboard();
			break;
		} else {
			printf("Wrong selection. Please try again.\n");
		}
	}
	
	printf("Calculating some vectors and matrices.\n");
	
	calculateAllocatedVector();
	calculateNeedMatrix();
	calculateMaxResourceVector();
	setCountAndRunningState();
	
	system("PAUSE");
	
	while(checkLoop){
		system("CLS");
		printf("Select work you want to do.");
		printf("\n1. Print all data.");
		printf("\n2. Check if system is safe and find the safe order of processes executing.");
		printf("\n3. Enter request and check if system can be responded.");
		printf("\n0. Exit.");
		printf("\nEnter your number of selection: ");
		scanf("%d", &selection);
		int ** b;
		int a[3][3] = {1, 4, 5, 1 ,1 ,1, 1, 1, 1};		
		switch(selection){
			case 1:
				printAllData();
				system("PAUSE");
				break;
			case 2:
				checkIfSystemIsSafe();
				system("PAUSE");
				break;
			case 3:
				checkRequest();			
				break;
			case 0:
				checkLoop = false;
				break;
			default:
				printf("\nYou entered wrong selection. Please select function to run again.");
				system("PAUSE");
				break;
		}
	}
}
