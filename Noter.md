# Model View Controller

Link til forklaring af Model View Controller: http://www.docjava.dk/patterns/model_view_controller/model_view_controller.htm

Model: Klasse der indeholder de relevante data, og laver udregninger på dem.

View: Klasse der viser en hel eller delvis repræsentation af de data der er i modellen. View skal opdatere, når der sker ændringer i modellen. View er vores user interface (UI), det tager sig af layout og styling, men ikke af logik og funktioner.

Controller: Klasse der er ligesom kontrolcenteret, den modtager input fra view og kalder på funktioner i model, og sender data tilbage til view.

Main: En java fil der initialiserer de tre ovennævnte klasse og begynder programmet med launch.

Eksempel af hvad der sker når der bruges Model-view-controller:
1. Brugeren interagerer med vinduet som view laver.
2. Controller får brugerens input fra view, og tolker kommandoen.
3. Controller fortæller model at den skal f.eks. hente data, ændre data, opdatere data osv.
4. Model udregner logikken og returnerer data, eller opdatere sine egne data.
5. Controller modtager data fra Model og sender det til View, som opdatere sig selv, så ændringen vises på skærmen for brugeren.


# Aflevering
Afleveres d. 18. januar 2026.
- En rapport skrevet i LaTeX, afleveret som pdf.
- Zip fil indeholdende mindst den grundlæggende del (obligatorisk) som jar-fil med tilhørende README.md, men kan også indeholde den avancerede del (valgfrit) som en separat jar-fil og tilhørende README.md, alt sammen i én zip fil.


# Mødetider
Fælles mødetider for gruppen:
Bare skriv hvis du kommer forsent, det gør ikke noget!
- d. 12. januar, mandag kl. 12.
- d. 16. januar, fredag kl. 12.

PS: Disse mødetider kan ændres alt efter behov.


# Brug af Git i VSCode

## Opsætning

Link til artikel om Source Control: [Link](https://code.visualstudio.com/docs/sourcecontrol/overview)


Man kan bruge Source Control (I VSCode menu i venstre side, er der muligvis en ikon der ligner forbindelser mellem tre cirkler) til at gøre alt hvad vi før har gjort i terminalen, men for at bruge den skal man først gøre følgende:


1. Åbn terminalen og skriv:

```
git config --global user.name "Github-Brugernavn"
```

I stedet for Github-Brugernavn, så skriv dit brugernavn du bruger på github.com.


2. Skriv i terminalen:

```
git config --global user.email useremail@email.com
```


I stedet for useremail@email.com, så skriv den email du bruger til github.com.

## Git i Source Control
Når du er færdig med forrige sektion, så kan du bruge Source Control til at bruge git meget nemmere.

Hvis du vil fetch:
- Tryk på Source Control i menuen i venstre side.
- I "Graph"-sektionen er der to pile der peger nedad, den første har en tool-tip der siger "Fetch From All Remotes". Hvis du trykker på den får du alle de opdateringer der er lavet på vores repository.

Hvis du vil pull:
- Tryk på Source Control i menuen i venstre side.
- I "Graph"-sektionen er der to pile der peger nedad, den anden har en tool-tip der siger "Pull". Tryk på den for at acceptere nye opdateringer lavet på vores repository.

Hvis du vil gemme dine egne ændringer:
- I "Changes"-sektionen, så ser du dine ændringer under sektionen "Changes" (Ja, sektionen Changes har en undersektion der hedder Changes), disse kan du tillægge til din "Stage" for at udvælge dem til at blive committed, du trykker bare på plus-ikonen der kaldes "Stage Changes".
- Når du har ændringer i sektionen "Staged Changes" Så kan du skrive en besked der beskriver hvad du har ændret i "Message"-inputfeltet ovenover.
- Når du har skrevet en besked, så kan du trykke på knappen "Commit".
- For at sende dine ændringer afsted, skal du trykke.

## Git på terminal
### For at tjekke om andre har lavet ændringer

```
git fetch
```


Hvis der kommer tekst frem på terminalen, så:
```
git pull
```


### For at gemme dit arbejde
Altid undersøg om andre har ændret noget i main branch:
```
git fetch
```


Hvis der er ændringer:
```
git pull
```


Undersøg hvilke filer du selv har ændret:
```
git status
```


Vælg de filer du har ændret:
```
git add filnavn.txt
```


(Her er filnavn.txt bare et eksempel).

"Gem" eller commit de ændringer du har lavet i den fil, eller de filer du har valgt:
```
git commit -m "Beskriv de ændringer du har lavet"
```


Send ændringerne til vores fælles repository:
```
git push
```


For at få hjælp til kommandoer i selve terminalen:
```
git help
```


Brug Google til at finde løsninger til eventuelle problemer.

Alle er velkomne til at dele problemer og løsninger her!
F.eks. "Hvis terminal siger X efter git push, så bare gør Y."

## Tip til workflow: Git branch

Det kan være brugbart at lave sin egen "Branch". Dette er fordi, hvis alle laver ændringer på samme tid, så kan du miste dine ændringer, eller der kan komme rod i projektet og forårsage unødvendige hovedpiner individuelt.


Du kan lave en branch hver gang du vil arbejde på noget, som du senere vil "merge" ind i projektets main branch.


Som eksempel, kunne man begynde at arbejde på at lave et vindue der kommer når programmet starter, men man ved ikke om det virker endnu, så man laver en ny branch der hedder "Vindue" (Eller andet man synes er mere beskrivende).


Når man så har fået programmet i sin Vindue-branch til at åbne et vindue ved program-start, så kan man *merge* sin branch til main, uden at alle andres ændringer laver rod i det.


Der er flere videoer på youtube der er meget bedre til at forklare git branch og git merge! Så der er altid hjælp at hente.


Herunder beskrives lidt om hvordan man laver en branch

### Hvordan man laver en ny branch, med Source Control
Forsøg at åbne Source Control (Hvis man flytter musen henover hver ikon, så vil deres navne poppe frem, så kan man let finde Source Control) fra din Visual Studio Code menu på venstrehånd.


Der burde komme en sidemenu frem på venstrehånd der hedder "Source Control", her kan du trykke på "Changes".


Herfra kan du flytte din mus henover changes og se flere knapper der fremvises, tryk på de tre prikker der hedder "More Actions..." (Hvis jeres Visual Studio Code altså er på engelsk). 


Herfra får du en pop-up menu hvor du kan trykke "Branch" > "Create Branch...", og skrive dét navn du vil give din branch.


Hvis du nu trykker på de tre prikker (More Actions...) igen, så kan du trykke "Checkout to..." og vælge dén branch du har lavet.


Når du har gjort dette, så burde du kunne lave ændringer i filerne, og commit og push, uden at det rører ved main-branch, det ændrer kun filerne i din selv-lavede branch!


Senere kan du så merge din branch med main branch.


### Hvordan man laver en ny branch, i terminalen
```
git branch NyBranchNavn
```


### Skift til en anden branch
```
git checkout BranchNavn
```


### Hvordan sletter man en branch, i terminalen
```
git branch -D BranchNavn
```


### Hvordan man merger sin branch med main-branch

I Source Control, tryk "Changes", tryk på de tre prikker der kommer frem når du holder musen over "Changes". Tryk "Branch" > "Merge...". Derefter vælger du dén branch du vil merge din nuværende branch med.


### Tjek hvilken branch du er på

- I Source Control: Tryk på "Changes" og flyt din mus henover den blå "Commit"-knap, uden at trykke på noget, så vil der komme en tool-tip frem der siger "Commit changes on "branch-name"", hvor branch-name er navnet på dén branch du er på nu. Hvis ikke du har lavet en ny branch eller "Checkout to..." til en ny branch, så vil der stå: "Commit changes on "main"".


- I terminalen: Skriv ``git status``. I terminalen vil der lige efter din kommando stå: "On branch main", hvis du er på main branch. Hvis du er på en anden branch, vil navnet på dette branch stå i stedet for "main".
