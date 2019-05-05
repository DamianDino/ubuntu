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

(define (top stack) (car stack))

(define (pop stack) (rest stack))
      

;; TODO 2:
;; Alegeți metoda de reprezentare a unei mașini stivă.
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
              (+ 1 (helper-index symbol (cdr symbols)))))

(define (get-symbol-index symbol)
  (helper-index symbol symbols))

;; Definiți funcția update-stack-machine care intoarce o noua mașina stivă
;; înlocuind componenta corespondentă simbolului cu item-ul dat în paremetri.
;; > (get-varnames (update-stack-machine "new-varnames" 'CO-VARNAMES stack-machine))
;; "new-varnames"
;; > (get-varnames (update-stack-machine "new-names" 'CO-NAMES stack-machine))
;; "new-names"
(define (update-stack-machine item symbol stack-machine)
  ;(display (get-symbol-index symbol))
  ;(if (equal? 
  (append (take stack-machine (get-symbol-index symbol)) (list item) (drop stack-machine   (add1 (get-symbol-index symbol)))))
 ; (cons (take stack-machine (sub1 (get-symbol-index symbol))) (cons item (drop stack-machine (add1 (get-symbol-index symbol))))))
  
;; Definiți funcția push-exec-stack care primește o masină stivă și o valoare
;; și intoarce o noua mașina unde valoarea este pusă pe stiva de execuție
(define (push-exec-stack value stack-machine)
  (display value)
  (update-stack-machine (push value (get-stack stack-machine)) 'STACK stack-machine))

;;  Definiți funcția pop-exec-stack care primește o masină stivă
;;  și intoarce o noua mașina aplicând pop pe stiva de execuție.
(define (pop-exec-stack stack-machine)
  (update-stack-machine (pop  (get-stack stack-machine)) 'STACK stack-machine))

;; TODO 4:
;; Definiți funcția run-stack-machine care execută operații pană epuizează co-code.

(define (load-const value stack-machine)
  (update-stack-machine (add1 (get-IC stack-machine)) 'INSTRUCTION-COUNTER (push-exec-stack (hash-ref (get-consts stack-machine) value) stack-machine)))
 ; (push-exec-stack (hash-ref (get-consts stack-machine) value) stack-machine)
  ;(update-stack-machine (add1 (get-IC stack-machine)) 'INSTRUCTION-COUNTER stack-machine))

(define (store-fast idx stack-machine)
  ;(update-stack-machine (add1 (get-IC stack-machine)) 'INSTRUCTION-COUNTER (pop-exec-stack (hash-set (get-varnames stack-machine) (top stack-machine) (top (get-stack stack-machine))))))

  (update-stack-machine (hash-set (get-varnames stack-machine) idx (car (top stack-machine)))
                        'CO-VARNAMES
  (update-stack-machine (add1 (get-IC stack-machine)) 'INSTRUCTION-COUNTER (pop-exec-stack stack-machine))))

(define (load-fast value stack-machine)
 (update-stack-machine (add1 (get-IC stack-machine)) 'INSTRUCTION-COUNTER (push-exec-stack (hash-ref (get-varnames stack-machine) value) stack-machine)))

(define (binary-add stack-machine acc)
  (let ((acc 0))
 (+ acc (top stack-machine) (top (cdr stack-machine)))
 (pop-exec-stack stack-machine)
 (pop-exec-stack stack-machine)
 (push-exec-stack acc stack-machine)))

(define (run-stack-machine stack-machine)
  (display stack-machine)
     (cond
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'RETURN_VALUE) stack-machine) 
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'LOAD_CONST) (run-stack-machine (load-const (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'STORE_FAST) (run-stack-machine (store-fast (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))
       ((equal? (car (list-ref (get-code stack-machine) (get-IC stack-machine))) 'LOAD_FAST) (run-stack-machine (load-fast (cdr (list-ref (get-code stack-machine) (get-IC stack-machine))) stack-machine)))))