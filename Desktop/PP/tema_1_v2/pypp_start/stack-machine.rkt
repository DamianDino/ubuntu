#lang racket
(require "opcodes.rkt")
(provide make-stack-machine)
(provide run-stack-machine)
(provide get-stack)
(provide get-varnames)
(provide get-consts)
(provide get-names)
(provide get-code)
(provide get-IC)
(provide empty-stack)
(provide make-stack)
(provide push)
(provide pop)
(provide top)


;; TODO 1:
;; Alegeți metoda de reprezentarea a unei stive.
;; Implementați:
(define empty-stack '())

(define (make-stack) empty-stack)

(define (push element stack) (cons element stack))

(define (top stack)
  (if (null? stack)
      stack
      (car stack)))

(define (top1 stack)
  (if (null? stack)
      stack
      (car (cdr stack))))
  
(define (pop stack) (rest stack))
      
;; TODO 2:
;; Alegeți metoda de reprezentare a unei mașini stivă. -> lista
;; Definiți make-stack-machine, acesta trebuie sa primeasca cele 4 segmente de date
;; Veți avea nevoie de o stivă pentru execuție și un counter ca să stiți
;; la ce instrucțiune sunteți.
(define (make-stack-machine stack co-varnames co-consts co-names co-code IC)
  (list stack co-varnames co-consts co-names co-code IC))

;; Definiți funcțiile `get-varnames`, `get-consts`, `get-names`,
;; `get-code`, `get-stack`, `get-IC` care primesc o mașina stivă și întorc
;; componenta respectivă

;; ex:
;; > (get-varnames (make-stack-machine empty-stack 'dummy-co-varnames (hash) (hash) (list) 0))
;; 'dummy-co-varnames
(define (get-varnames stack-machine)
  (second stack-machine))

;; ex:
;; > (get-consts (make-stack-machine empty-stack (hash) 'dummy-co-consts (hash) (list) 0))
;; 'dummy-co-consts
(define (get-consts stack-machine)
  (third stack-machine))

;; ex:
;; > (get-names (make-stack-machine empty-stack (hash) (hash) 'dummy-co-names (list) 0))
;; 'dummy-co-names
(define (get-names stack-machine)
  (fourth stack-machine))

;; ex:
;; > (get-code (make-stack-machine empty-stack (hash) (hash) (hash) 'dummy-co-code 0))
;; dummy-co-code
(define (get-code stack-machine)
  (fifth stack-machine))

;; Întoarce stiva de execuție.
;; ex:
;; > (get-stack (make-stack-machine 'dummy-exec-stack (hash) (hash) (hash) (list) 0))
;; dummy-exec-stack
(define (get-stack stack-machine)
  (car stack-machine))

;; Întoarce instruction counterul.
;; ex:
;; > (get-IC (make-stack-machine empty-stack (hash) (hash) (hash) (list) 0))
;; 0
(define (get-IC stack-machine)
  (last stack-machine))

(define symbols (list 'STACK 'CO-VARNAMES 'CO-CONSTS 'CO-NAMES 'CO-CODE 'INSTRUCTION-COUNTER))

;; TODO 3:
;; Definiți funcția get-symbol-index care gasește index-ul simbolului in listă.
(define (helper-index symbol symbols)
  (if (equal? symbol (car symbols))
              0
              (add1 (helper-index symbol (cdr symbols)))))

(define (get-symbol-index symbol)
  (helper-index symbol symbols))

;; Definiți funcția update-stack-machine care intoarce o noua mașina stivă
;; înlocuind componenta corespondentă simbolului cu item-ul dat în paremetri.
;; > (get-varnames (update-stack-machine "new-varnames" 'CO-VARNAMES stack-machine))
;; "new-varnames"
;; > (get-varnames (update-stack-machine "new-names" 'CO-NAMES stack-machine))
;; "new-names"
(define (update-stack-machine item symbol stack-machine)
  (append (take stack-machine (get-symbol-index symbol))
          (list item)
          (drop stack-machine (add1 (get-symbol-index symbol)))))
  
;; Definiți funcția push-exec-stack care primește o masină stivă și o valoare
;; și intoarce o noua mașina unde valoarea este pusă pe stiva de execuție
(define (push-exec-stack value stack-machine)
  (update-stack-machine (push value (get-stack stack-machine)) 'STACK stack-machine))

;;  Definiți funcția pop-exec-stack care primește o masină stivă
;;  și intoarce o noua mașina aplicând pop pe stiva de execuție.
(define (pop-exec-stack stack-machine)
  (update-stack-machine (pop (get-stack stack-machine)) 'STACK stack-machine))

;; TODO 4:
;; Definiți funcția run-stack-machine care execută operații pană epuizează co-code.

;; LOAD-CONST
(define (load-const value stack-machine)
  (update-stack-machine (add1 (get-IC stack-machine))
                        'INSTRUCTION-COUNTER
                        (push-exec-stack (hash-ref (get-consts stack-machine) value) stack-machine)))

;; STORE-FAST
(define (store-fast idx stack-machine)
  (update-stack-machine (hash-set (get-varnames stack-machine) idx (car (top stack-machine)))
                        'CO-VARNAMES
                        (update-stack-machine (add1 (get-IC stack-machine)) 'INSTRUCTION-COUNTER (pop-exec-stack stack-machine))))

;; LOAD-FAST
(define (load-fast value stack-machine)
 (update-stack-machine (add1 (get-IC stack-machine))
                       'INSTRUCTION-COUNTER
                       (push-exec-stack (hash-ref (get-varnames stack-machine) value) stack-machine)))

;; BINARY-ADD
(define (add-helper stack-machine acc)
  (+ acc (top (get-stack stack-machine)) (top1 (get-stack stack-machine))))
 
(define (binary-add stack-machine)
  (update-stack-machine (add1 (get-IC stack-machine))
                        'INSTRUCTION-COUNTER
                        (push-exec-stack (add-helper stack-machine 0) (pop-exec-stack (pop-exec-stack stack-machine)))))

;; BINARY-SUBTRACT
(define (subtract-helper stack-machine acc)
  (+ acc (- (top1 (get-stack stack-machine)) (top (get-stack stack-machine)) )))

(define (binary-subtract stack-machine)
  (update-stack-machine (add1 (get-IC stack-machine))
                        'INSTRUCTION-COUNTER
                        (push-exec-stack (subtract-helper stack-machine 0) (pop-exec-stack (pop-exec-stack stack-machine)))))

;; BINARY-MODULO
(define (modulo-helper stack-machine acc)
  (modulo acc (top (get-stack stack-machine))))
 
(define (binary-modulo stack-machine)
  (update-stack-machine (add1 (get-IC stack-machine))
                        'INSTRUCTION-COUNTER
                        (push-exec-stack (modulo-helper stack-machine (top1 (get-stack stack-machine)))
                                         (pop-exec-stack (pop-exec-stack stack-machine)))))

;; IF

(define (jump-absolut target stack-machine)
  (update-stack-machine (/ target 2) 'INSTRUCTION-COUNTER stack-machine))

(define (compare-op index stack-machine)
  (cond
    ((equal? index 0) (update-stack-machine (add1 (get-IC stack-machine))
                                            'INSTRUCTION-COUNTER
                                            (push-exec-stack (< (top1 (get-stack stack-machine)) (top (get-stack stack-machine)))
                                                             (pop-exec-stack (pop-exec-stack stack-machine)))))
    ((equal? index 1) (update-stack-machine (add1 (get-IC stack-machine))
                                            'INSTRUCTION-COUNTER
                                            (push-exec-stack (<= (top1 (get-stack stack-machine)) (top (get-stack stack-machine)))
                                                             (pop-exec-stack (pop-exec-stack stack-machine)))))
    ((equal? index 2) (update-stack-machine (add1 (get-IC stack-machine))
                                            'INSTRUCTION-COUNTER
                                            (push-exec-stack (= (top1 (get-stack stack-machine)) (top (get-stack stack-machine)))
                                                             (pop-exec-stack (pop-exec-stack stack-machine)))))
    ((equal? index 3) (update-stack-machine (add1 (get-IC stack-machine))
                                            'INSTRUCTION-COUNTER
                                            (push-exec-stack (not (= (top1 (get-stack stack-machine)) (top (get-stack stack-machine))))
                                                             (pop-exec-stack (pop-exec-stack stack-machine)))))
    ((equal? index 4) (update-stack-machine (add1 (get-IC stack-machine))
                                            'INSTRUCTION-COUNTER
                                            (push-exec-stack (> (top1 (get-stack stack-machine)) (top (get-stack stack-machine)))
                                                             (pop-exec-stack (pop-exec-stack stack-machine)))))
    ((equal? index 5) (update-stack-machine (add1 (get-IC stack-machine))
                                            'INSTRUCTION-COUNTER
                                            (push-exec-stack (>= (top1 (get-stack stack-machine)) (top (get-stack stack-machine)))
                                                             (pop-exec-stack (pop-exec-stack stack-machine)))))
    ((equal? index 6) (update-stack-machine (add1 (get-IC stack-machine))
                                            'INSTRUCTION-COUNTER
                                            (push-exec-stack (member (top1 (get-stack stack-machine)) (top (get-stack stack-machine)))
                                                             (pop-exec-stack (pop-exec-stack stack-machine)))))
    ((equal? index 7) (update-stack-machine (add1 (get-IC stack-machine))
                                            'INSTRUCTION-COUNTER
                                            (push-exec-stack (not (member (top1 (get-stack stack-machine)) (top (get-stack stack-machine))))
                                                             (pop-exec-stack (pop-exec-stack stack-machine)))))))


(define (jump-if-true target stack-machine)
  (if (top (get-stack stack-machine))
      (jump-absolut target (pop-exec-stack stack-machine))
      (update-stack-machine (add1 (get-IC stack-machine))
                            'INSTRUCTION-COUNTER
                            (pop-exec-stack stack-machine))))

(define (jump-if-false target stack-machine)
  (if (not (top (get-stack stack-machine)))
      (jump-absolut target (pop-exec-stack stack-machine))
      (update-stack-machine (add1 (get-IC stack-machine))
                            'INSTRUCTION-COUNTER
                            (pop-exec-stack stack-machine))))

;; FOR LOOP
(define (for-helper stack-machine)
  (define aux (top (get-stack stack-machine)))
  (push-exec-stack (car aux) (push-exec-stack (cdr aux) (pop-exec-stack stack-machine))))
  

(define (for-iter delta stack-machine)
  (if (null? (top (get-stack stack-machine)))
      (update-stack-machine (+ (/ delta 2) 1 (get-IC stack-machine))
                            'INSTRUCTION-COUNTER
                             (pop-exec-stack stack-machine))
      (update-stack-machine (add1 (get-IC stack-machine))
                            'INSTRUCTION-COUNTER
                            (for-helper stack-machine)))) 


;; BONUS
(define (load-global index stack-machine)
  (update-stack-machine (add1 (get-IC stack-machine))
                        'INSTRUCTION-COUNTER
                        (push-exec-stack (hash-ref (get-names stack-machine) index) stack-machine)))

(define bonus-functions
  (hash "print" writeln "range" range "sqrt" sqrt "prod" *))

(define (multiple-pop stack-machine number)
  (if (zero? number)    ;; pop until number reaches 0
      stack-machine
      (multiple-pop (pop-exec-stack stack-machine) (sub1 number))))

(define (pop-top value stack-machine)
  (update-stack-machine (add1 (get-IC stack-machine))
                        'INSTRUCTION-COUNTER
                        (pop-exec-stack stack-machine)))

(define (call-function args stack-machine)
  (if (equal? (drop (get-stack stack-machine) args) "prod")  ;;the only function with > 2 args
      (update-stack-machine (add1 (get-IC stack-machine ))
                            'INSTRUCTION-COUNTER
                            (push-exec-stack (apply * 1 (take (get-stack stack-machine) args))
                                             (multiple-pop stack-machine (add1 args))))
      (update-stack-machine (add1 (get-IC stack-machine))
                            'INSTRUCTION-COUNTER
                            (push-exec-stack (apply (hash-ref bonus-functions (car (drop (get-stack stack-machine) args))) (take (get-stack stack-machine) args))
                                             (multiple-pop stack-machine (add1 args))))))  
  
;; MAIN
(define (run-stack-machine stack-machine)
  ;; check every operation and go to the corresponding function, while repeating run-stack-machine
     (cond
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'RETURN_VALUE) stack-machine) 
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'LOAD_CONST) (run-stack-machine (load-const (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'STORE_FAST) (run-stack-machine (store-fast (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'LOAD_FAST) (run-stack-machine (load-fast (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((or (equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'BINARY_ADD) (equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'INPLACE_ADD))
           (run-stack-machine (binary-add stack-machine)))
       ((or (equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'BINARY_SUBTRACT) (equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'INPLACE_SUBTRACT))
           (run-stack-machine (binary-subtract stack-machine)))
       ((or (equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'BINARY_MODULO) (equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'INPLACE_MODULO))
           (run-stack-machine (binary-modulo stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'COMPARE_OP) (run-stack-machine (compare-op (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'JUMP_ABSOLUTE) (run-stack-machine (jump-absolut (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'POP_JUMP_IF_TRUE) (run-stack-machine (jump-if-true (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'POP_JUMP_IF_FALSE) (run-stack-machine (jump-if-false (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'FOR_ITER) (run-stack-machine (for-iter (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'GET_ITER) (run-stack-machine (update-stack-machine (add1 (get-IC stack-machine)) 'INSTRUCTION-COUNTER stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'SETUP_LOOP) (run-stack-machine (update-stack-machine (add1 (get-IC stack-machine)) 'INSTRUCTION-COUNTER stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'POP_BLOCK) (run-stack-machine (update-stack-machine (add1 (get-IC stack-machine)) 'INSTRUCTION-COUNTER stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'LOAD_GLOBAL) (run-stack-machine (load-global (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'CALL_FUNCTION) (run-stack-machine (call-function (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'POP_TOP) (run-stack-machine (pop-top (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))))     