{-# OPTIONS_GHC -Wall #-}
{-# LANGUAGE EmptyDataDecls, MultiParamTypeClasses,
             TypeSynonymInstances, FlexibleInstances,
             InstanceSigs #-}


module Bloxorz where

import ProblemState

import qualified Data.Array as A
import Data.Matrix

{-
    Caracterele ce vor fi printate pentru fiecare tip de obiect din joc 
    Puteți înlocui aceste caractere cu orice, în afară de '\n'.
-}

hardTile :: Char
hardTile = '▒'

softTile :: Char
softTile = '='

block :: Char
block = '▓'

switch :: Char
switch = '±'

emptySpace :: Char
emptySpace = 'o'

winningTile :: Char
winningTile = '*'

{-
    Sinonim de tip de date pentru reprezetarea unei perechi (int, int)
    care va reține coordonatele de pe tabla jocului
-}

type Position = (Int, Int)

{-
    Direcțiile în care se poate mișcă blocul de pe tablă
-}

data Directions = North | South | West | East
    deriving (Show, Eq, Ord)

{-
    *** TODO ***

    Tip de date care va reprezenta plăcile care alcătuiesc harta și switch-urile
-}

data Cell = HardTile | SoftTile | Block | Switch | EmptySpace | WinningTile
	deriving Eq

instance Show Cell where
	show HardTile = [hardTile] 
	show SoftTile = [softTile] 
	show Block = [block]
	show Switch = [switch] 
	show EmptySpace = [emptySpace] 
	show WinningTile = [winningTile]

{-                                                                                                                                                                                   
    *** TODO ***

    Tip de date pentru reprezentarea nivelului curent
-}

-- switchInfo : pozitia switchului, 0/1 inactiv/activ, lista cu pozitiile pe care le activeaza

data Level = Level { board :: Matrix Cell, blockCoord :: Position, blockPositions :: [Position], nrSwitches :: Int, switchInfo :: [(Position, Int, [Position])] } 
	deriving Eq


{-
    *** Opțional *** 
  
    Dacă aveți nevoie de o funcționalitate particulară, 
    instantiati explicit clasele Eq și Ord pentru Level. 
    În cazul acesta, eliminați deriving (Eq, Ord) din Level. 
-}

-- instance Eq Level where
--     (==) = undefined

instance Ord Level where
	compare _ _ = 1 `compare` 2

{-
    *** TODO ***

    Instantiati Level pe Show. 

    Atenție! String-ul returnat va fi urmat și precedat de un rând nou. 
    În cazul în care jocul este câștigat, la sfârșitul stringului se va mai
    concatena mesajul "Congrats! You won!\n". 
    În cazul în care jocul este pierdut, se va mai concatena "Game Over\n". 
-}

instance Show Level where
	show level = "\n" ++ (filter (not . (`elem` "() ")) . prettyMatrix $ (board level))

{-
    *** TODO ***

    Primește coordonatele colțului din dreapta jos a hartii și poziția inițială a blocului.
    Întoarce un obiect de tip Level gol.
    Implicit, colțul din stânga sus este (0, 0).
-}


emptyLevel :: Position -> Position -> Level
emptyLevel (linii, coloane) (xblockCoord, yblockCoord) = Level (matrix (linii+1) (coloane+1) (\(i,j) -> (if (i == (xblockCoord+1) && j == (yblockCoord+1)) then Block else EmptySpace))) (xblockCoord, yblockCoord) [(xblockCoord, yblockCoord)] 0 []


{-
    *** TODO ***

    Adaugă o celulă de tip Tile în nivelul curent.
    Parametrul char descrie tipul de tile adăugat: 
        'H' pentru tile hard 
        'S' pentru tile soft 
        'W' pentru winning tile 
-}

addTile :: Char -> Position -> Level -> Level
addTile 'H' (xH, yH) a = if (xH == fst((blockCoord a)) && yH == snd((blockCoord a))) then a else Level (setElem HardTile (xH+1, yH+1) (board a)) (blockCoord a) [blockCoord a] (nrSwitches a) (switchInfo a)
addTile 'S' (xS, yS) a = Level (setElem SoftTile (xS+1, yS+1) (board a)) (blockCoord a) [(blockCoord a)] (nrSwitches a) (switchInfo a)
addTile 'W' (xW, yW) a = Level (setElem WinningTile (xW+1, yW+1) (board a)) (blockCoord a) [(blockCoord a)] (nrSwitches a) (switchInfo a)
addTile  _   _       a = error "This tile doesn't exist, sorry"

{-
    *** TODO ***

    Adaugă o celulă de tip Switch în nivelul curent.
    Va primi poziția acestuia și o listă de Position
    ce vor desemna pozițiile în care vor apărea sau 
    dispărea Hard Cells în momentul activării/dezactivării
    switch-ului.
-}

addSwitch :: Position -> [Position] -> Level -> Level
addSwitch (xSwitch, ySwitch) b a = Level (setElem Switch (xSwitch+1, ySwitch+1) (board a)) (blockCoord a) [(blockCoord a)] ((nrSwitches a)+1) (((xSwitch, ySwitch), 0, b): switchInfo a)

{-
    === MOVEMENT ===
-}

{-
    *** TODO ***

    Activate va verifica dacă mutarea blocului va activa o mecanică specifică. 
    În funcție de mecanica activată, vor avea loc modificări pe hartă. 
-}

getFst :: (a,b,c) -> a
getFst (a, _, _) = a

getSnd :: (a,b,c) -> b
getSnd (_, b, _) = b

getLast :: (a,b,c) -> c
getLast (_, _, c) = c

--activate :: Position -> Level -> Level
--activate (x, y) a -- Level (board a) (blockCoord a) [(blockCoord a)] (nrSwitches a) plm
--	| ((getSnd (head (switchInfo a))) == 1) = Level (setElem HardTile (x+1, y+1) (board a)) (blockCoord a) [(blockCoord a)] (nrSwitches a) (switchInfo a)
--	| otherwise =  Level (setElem EmptySpace (x+1, y+1) (board a)) (blockCoord a) [(blockCoord a)] (nrSwitches a) (switchInfo a)

activate :: Cell -> Level -> Level
activate cell a 
	| ((getSnd (head (switchInfo a))) == 1) = Level (setElem cell ((fst (head (getLast (head (switchInfo a))))), (snd (head (getLast (head (switchInfo a)))))) (board a)) (blockCoord a) [(blockCoord a)] (nrSwitches a) (switchInfo a)
	| otherwise = Level (setElem cell ((fst (head (getLast (head (switchInfo a))))), (snd (head (getLast (head (switchInfo a)))))) (board a)) (blockCoord a) [(blockCoord a)] (nrSwitches a) (switchInfo a)
{-
    *** TODO ***

    Mișcarea blocului în una din cele 4 direcții 
    Hint: Dacă jocul este deja câștigat sau pierdut, puteți lăsa nivelul neschimbat.
-}

move :: Directions -> Level -> Level
move d a
-- cand blocul este in picioare
	| (d == North && (length (blockPositions a) == 1)) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))-1), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))), (snd (blockCoord a))+1) (board a)))) (blockCoord a) ([(((fst (blockCoord a))-1), (snd (blockCoord a))+1)]++[(((fst (blockCoord a))), (snd (blockCoord a))+1)]++ blockPositions a) (nrSwitches a) (switchInfo a)      
	| (d == South && (length (blockPositions a) == 1)) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))+2), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))+3), (snd (blockCoord a))+1) (board a)))) (blockCoord a) ([(((fst (blockCoord a))+3), (snd (blockCoord a))+1)]++[(((fst (blockCoord a))+2), (snd (blockCoord a))+1)]++ blockPositions a) (nrSwitches a) (switchInfo a)      
	| (d == West  && (length (blockPositions a) == 1)) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))+1), (snd (blockCoord a))-1) (setElem Block ((fst (blockCoord a)) +1, (snd (blockCoord a))) (board a)))) (blockCoord a) ([(((fst (blockCoord a))+1), (snd (blockCoord a)))]++[(((fst (blockCoord a))+1), (snd (blockCoord a))-1)]++ blockPositions a) (nrSwitches a) (switchInfo a)      
	| (d == East  && (length (blockPositions a) == 1)) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))+1), (snd (blockCoord a))+3) (setElem Block ((fst (blockCoord a)) +1, (snd (blockCoord a))+2) (board a)))) (blockCoord a) ([(((fst (blockCoord a))+1), (snd (blockCoord a))+2)]++[(((fst (blockCoord a))+1), (snd (blockCoord a))+3)]++ blockPositions a) (nrSwitches a) (switchInfo a)      
-- cand blocul este culcat orientat N-S
 	| (d == North && (length (blockPositions a) == 2) && (snd (head (blockPositions a))) == (snd (last (blockPositions a)))) = Level (setElem Block (((fst (blockCoord a))-1), (snd (blockCoord a))+1) (setElem HardTile ((fst (blockCoord a)), (snd (blockCoord a))+1) (setElem HardTile ((fst (blockCoord a)) +1, (snd (blockCoord a))+1) (board a)))) (blockCoord a) ([(((fst (blockCoord a))-1), (snd (blockCoord a))+1)]++(blockPositions a)) (nrSwitches a) (switchInfo a)
	| (d == East  && (length (blockPositions a) == 2) && (snd (head (blockPositions a))) == (snd (last (blockPositions a)))) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem HardTile (((fst (blockCoord a))+2), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))+1), (snd (blockCoord a))+2) (setElem Block (((fst (blockCoord a))+2), (snd (blockCoord a))+2) (board a))))) (blockCoord a) ([(((fst (blockCoord a))+2), (snd (blockCoord a))+2)]++[(((fst (blockCoord a))+1), (snd (blockCoord a))+2)]++blockPositions a) (nrSwitches a) (switchInfo a)
	| (d == South && (length (blockPositions a) == 2) && (snd (head (blockPositions a))) == (snd (last (blockPositions a)))) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem HardTile (((fst (blockCoord a))), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))+2), (snd (blockCoord a))+1) (board a)))) (blockCoord a) ([(((fst (blockCoord a))+2), (snd (blockCoord a))+1)]++blockPositions a) (nrSwitches a) (switchInfo a)
	| (d == West  && (length (blockPositions a) == 2) && (snd (head (blockPositions a))) == (snd (last (blockPositions a)))) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem HardTile (((fst (blockCoord a))+2), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))+1), (snd (blockCoord a))) (setElem Block (((fst (blockCoord a))+2), (snd (blockCoord a))) (board a))))) (blockCoord a) ([(((fst (blockCoord a))+1), (snd (blockCoord a)))]++[(((fst (blockCoord a))+2), (snd (blockCoord a)))]++blockPositions a) (nrSwitches a) (switchInfo a)
-- cand blocul este culcat orientat W-E
	| (d == North && (length (blockPositions a) == 2) && (fst (head (blockPositions a))) == (fst (last (blockPositions a)))) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+2) (setElem Block (((fst (blockCoord a))), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))), (snd (blockCoord a))+2) (board a))))) (blockCoord a) ([(((fst (blockCoord a))), (snd (blockCoord a))+1)]++[(((fst (blockCoord a))), (snd (blockCoord a))+2)]++blockPositions a) (nrSwitches a) (switchInfo a)
	| (d == East  && (length (blockPositions a) == 2) && (fst (head (blockPositions a))) == (fst (last (blockPositions a)))) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+2) (setElem Block (((fst (blockCoord a))+1), (snd (blockCoord a))+3) (board a)))) (blockCoord a) ([(((fst (blockCoord a))+1), (snd (blockCoord a))+3)]++blockPositions a) (nrSwitches a) (switchInfo a)
	| (d == South && (length (blockPositions a) == 2) && (fst (head (blockPositions a))) == (fst (last (blockPositions a)))) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+2) (setElem Block (((fst (blockCoord a))+2), (snd (blockCoord a))+1) (setElem Block (((fst (blockCoord a))+2), (snd (blockCoord a))+2) (board a))))) (blockCoord a) ([(((fst (blockCoord a))+2), (snd (blockCoord a))+1)]++[(((fst (blockCoord a))+2), (snd (blockCoord a))+2)]++blockPositions a) (nrSwitches a) (switchInfo a)
	| (d == East  && (length (blockPositions a) == 2) && (fst (head (blockPositions a))) == (fst (last (blockPositions a)))) = Level (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+1) (setElem HardTile (((fst (blockCoord a))+1), (snd (blockCoord a))+2) (setElem Block (((fst (blockCoord a))+1), (snd (blockCoord a))) (board a)))) (blockCoord a) ([(((fst (blockCoord a))+1), (snd (blockCoord a))+3)]++blockPositions a) (nrSwitches a) (switchInfo a)

{-
    *** TODO ***

    Va returna True dacă jocul nu este nici câștigat, nici pierdut.
    Este folosită în cadrul Interactive.
-}

continueGame :: Level -> Bool
continueGame = undefined

{-
    *** TODO ***

    Instanțiați clasa `ProblemState` pentru jocul nostru. 
  
    Hint: Un level câștigat nu are succesori! 
    De asemenea, puteți ignora succesorii care 
    duc la pierderea unui level.
-}

instance ProblemState Level Directions where
    successors = undefined

    isGoal = undefined

    -- Doar petru BONUS
    -- heuristic = undefined
