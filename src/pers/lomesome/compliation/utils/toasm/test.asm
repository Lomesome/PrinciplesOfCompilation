global _start
extern _printf
section .data
msg1: db "leisure",10
msg_len1: equ $ - msg1
msg0: db "abc",10
msg_len0: equ $ - msg0
    fmt: db '%ld',0xa,0
section .bss
a: resb 4
t0: resb 4
section .text
_start:
    push rbp
   mov rax, 0x2000004
   mov rdi, 1
   mov rsi, msg0
   mov rdx, msg_len0
   syscall
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
    mov rdi, fmt
    mov rax, [rel a]
    mov rsi, rax
    mov rax, 0
    call _printf
   mov rax, 0x2000004
   mov rdi, 1
   mov rsi, msg1
   mov rdx, msg_len1
   syscall
    pop rbp
    mov rax, 60
    syscall
    ret
