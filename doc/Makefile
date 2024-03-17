
MAINDOC = thesis.tex
PDF = thesis.pdf
JOBNAME = $(PDF:.pdf=)
VIEWER=evince

.PHONY: all clean view

all:
	latexmk -pdflua $(MAINDOC) -jobname=$(JOBNAME)

clean:
	latexmk -pdflua -C $(MAINDOC) -jobname=$(JOBNAME)

view:
	$(VIEWER) $(PDF) >/dev/null 2>&1 &

