# RNA

# /scripts

Script take dot-brakcet formated file, finds unmatched nucleotides chains and print it.

rna_lines_extractor.py input_file_path [-v, --version] 

Version option is use to switch between RNAFold and ContextFold output. Default is RNAFold. [optional]
-v 0 For ContextFold output 
-v 1 fot RNAFold (is also deafult) 

Example files contain output from programs. Name of file accords to the name of the used program.





Algorithm

In order to work, without IDE, structure should look like this:<br>


-/out/ <br>
  -Main.java <br>
  -*other files* <br>
-/scripts/ <br>
  -rna_lines_extractor.py <br>

out is name with compiled project <br>
To run type (in "out" folder):  <br>
java Main ./path/to/fasta/entry <br>

# OPIS PL

Domyślnie algorymt przyjmuje plik z sekwencją (lub sekwencjami, wtedy analizuje je oddzielnie po koleji) RNA,  w formacie FASTA. 
Wyniki przetwarzania zapisywane są w folderze output_dir/nazwa_sekwencji/. 
Domyślnie ustawienia:
- RNAFold do predykcji struktury drugorzędowej
- Domyślnie program analizuje łańcuchy dla mi > 0.01 i długości większej lub równej 15.
- W celu optytmalizacji przetwarzania, domyslnie program przetwarza tylko cześć uzyskanych potencjalnych łańcuchów. przy uzykaniu X mozliwych dalszych łańcuchów, liczona jest średnia wartość mi i do dalszego przetwarzania trafiają tylko te łańcuchy o mi wyższym lub równym tej wartości. 


Program jest implementacją algorytmu przygotowanego przez dr. hab. inż. Agnieszkę Rybarczyk. 




