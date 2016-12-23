************************************************************************************************************

					Computer Security : Assignment 1

*************************************************************************************************************

Name  : Prashant Kadam

email : pkadam1@binghamton.edu

code tested on bingsuns? : Yes

How To Execute program : 

		To compile :
			make
		
	ava DesCounter <inputfile> <outputfile> <ENC/DEC>
The parameters description is as follows:
		<Inputfile>: input file name
		<outputfile>: output file name
		<ENC/DEC>: 1/0 – encryption or decryption
For example:
	//encrypt test.txt and store the encrypted content in file test.des
 		java DesCounter test.txt test.des 1 
 	//decrypt test.des and store the decrypted content in file out
 		java DesCounter test.des out 0 
 	//to compare file
 		diff test.txt out


*************************************************************************************************************

