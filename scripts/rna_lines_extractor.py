import argparse


# Class to store singular rna chains
class SingularChain:
    def __init__(self, chain_id, rna_seq, indexes_tuple):
        self.chain_id = chain_id
        self.rna_seq = rna_seq
        self.indexes_tuple = indexes_tuple

    def __str__(self):
        return "{id_r} \n{rna} \n{index}".format(id_r=self.chain_id, rna=self.rna_seq, index=self.indexes_tuple)


# Class to store RNA that script is currently computing
class FullRNA:
    # v=0 ->dla wyników z ContextFold bez konieczności usuwania ostaatnich dwóch nawiasów
    # v=1 ->dla wyników z RNAFold ( wymaga usunięcia ostaniego nawiasu)
    def __init__(self, input_file, v=0, output_file=False):
        self.version = v
        self.rna_chain_array = []
        self.rna_dot_bracket_seq = []

        self.simple_chains = []
        self.read(input_file)
        self.find_singular_chains()
        self.present_all()

    def read(self, input_file):
        stage = 1
        for l_nr, line in enumerate(input_file):
            if l_nr == 0 and line[0] == '>':
                continue
            if stage == 1:
                self.rna_chain_array = line[:-1]
                stage += 1
            else:
                if self.version > 0:
                    self.rna_dot_bracket_seq = self.trim(line[:-1])
                else:
                    self.rna_dot_bracket_seq = line[:-1]
#                 print(self.rna_dot_bracket_seq)

                # Break jest potrzebny ze względu na dodatkową linie w wyniku ContextFold
                # Jeśli pojawią się bardziej zaawansofane formaty, będzie trzeba to poprawić
                break

        # Presenting steps
        # print(self.rna_chain_array)
        # print(self.rna_dot_bracket_seq)

    @staticmethod
    def trim(input_string):
        index = input_string.rfind('(')
        return input_string[:index]

    def find_singular_chains(self):
        first_idx = 0
        last_idx = 0
        dot = False
        for i, value in enumerate(self.rna_dot_bracket_seq):
            if dot:
                if value == '.' and i == len(self.rna_dot_bracket_seq) - 1:
                    last_idx = i
                elif value =='.':
                    continue
                elif i == len(self.rna_dot_bracket_seq) - 1:
                    last_idx = i
                else:
                    last_idx = i
                dot = False
                # Presenting steps
                # print(self.rna_chain_array[first_idx:last_idx])
                # print(self.rna_dot_bracket_seq[first_idx:last_idx])
                self.simple_chains.append(
                    SingularChain(len(self.simple_chains), self.rna_chain_array[first_idx if first_idx == 0 else first_idx-1:last_idx+1],
                                  (first_idx if first_idx == 0 else first_idx-1, last_idx)))
            else:
                if value == '.':
                    dot = True
                    first_idx = i

    def present_all(self):
        for element in self.simple_chains:
            print(element)


def main(argv):
    input_file = open(argv.input_file, 'r')

    v = argv.version
    print("version {}".format(v))
    rna_object = FullRNA(input_file, v=v)


# Example of use
# python rna_lines_extractor.py example.txt
# Output is like (...) chain.
if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('input_file')
    parser.add_argument('-v', '--version', type=int, default=0, choices=[0, 1, 2])
    args = parser.parse_args()
    main(args)
