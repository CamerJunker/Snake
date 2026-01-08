# Model View Controller

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
