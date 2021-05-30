global _start
extern _printf
section .data
msg0: db "a的值",10
msg_len0: equ $ - msg0
    fmt: db '%ld',0xa,0
section .bss
a: resb 4
t0: resb 4
t1: resb 4
t2: resb 4
section .text
_start:
    push rbp
    mov rax, 99
    mov rbx, 2
    mul rbx
    mov [rel t0], rax
    mov rax, [rel t0]
    mov [rel a], rax
   mov rax, 0x2000004
   mov rdi, 1
   mov rsi, msg0
   mov rdx, msg_len0
   syscall
    mov rax, [rel a]
    mov rbx, 100
    cmp rax, rbx
jg TURN0
jmp TURN1
TURN0:
    mov rax, [rel a]
    mov rbx, 200
    cmp rax, rbx
jl TURN2
jmp TURN3
TURN2:
    mov rax, [rel a]
    mov rbx, 1
    add rax, rbx
    mov [rel t1], rax
    mov rax, [rel t1]
    mov [rel a], rax
    mov rdi, fmt
    mov rax, [rel a]
    mov rsi, rax
    mov rax, 0
    call _printf
jmp TURN0
TURN3:
jmp TURN4
TURN1:
    mov rax, [rel a]
    mov rbx, 0
    cmp rax, rbx
jg TURN5
jmp TURN4
TURN5:
    mov rax, [rel a]
    mov rbx, 1
    sub rax, rbx
    mov [rel t2], rax
    mov rax, [rel t2]
    mov [rel a], rax
    mov rdi, fmt
    mov rax, [rel a]
    mov rsi, rax
    mov rax, 0
    call _printf
jmp TURN1
TURN4:
    pop rbp
    mov rax, 60
    syscall
    ret
