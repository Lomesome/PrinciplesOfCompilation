global _start
extern _printf
section .data
    fmt: db '%ld',0xa,0
section .bss
a: resb 4
t0: resb 4
section .text
_start:
    push rbp
    mov rax, 100
    mov [rel a], rax
TURN2:
    mov rax, [rel a]
    mov rbx, 2
    cmp rax, rbx
jge TURN0
jmp TURN1
TURN0:
    mov rax, [rel a]
    mov rbx, 3
    sub rax, rbx
    mov [rel t0], rax
    mov rax, [rel t0]
    mov [rel a], rax
jmp TURN2
TURN1:
    pop rbp
    mov rax, 60
    syscall
    ret
