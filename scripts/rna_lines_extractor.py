import argparse


# Class to store singular rna chains
class SingularChain:
    def __init__(self, chain_id, rna_seq, indexes_tuple):
        self.chain_id = chain_id
        self.rna_seq = rna_seq
        self.indexes_tuple = indexes_tuple

    def __str__(self):
        return "ID: {id_r} \n{rna} \n{index} \n".format(id_r=self.chain_id, rna=self.rna_seq, index=self.indexes_tuple)

# Class to store RNA that script is currently computing
class FullRNA:
    # v=0 ->wynik z ContextFold bez konieczności usuwania ostaatnich dwóch nawiasów
    # v=1 ->wynik z RNAFold
    def __init__(self, input_file, v=1, output_file=False):
        self.version = v
        self.rna_chain_array = []
        self.rna_dot_bracket_seq = []

        self.simple_chains = []
        self.read(input_file)
        self.find_singular_chains()
        self.present_all()

    def read(self, input_file):
        for l_nr, line in enumerate(input_file):
            if l_nr == 0:
                self.rna_chain_array = line[:-1]
            else:
                if self.version == 1:
                    self.rna_dot_bracket_seq = self.trim(line[:-1])
                else:

                    self.rna_dot_bracket_seq = line[:-1]

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
                if value == '.':
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
                    SingularChain(len(self.simple_chains), self.rna_chain_array[first_idx:last_idx],
                                  (first_idx, last_idx)))
            else:
                if value == '.':
                    dot = True
                    first_idx = i

    def present_all(self):
        for element in self.simple_chains:
            print(element)


def main(argv):
    input_file = open(argv.input_file, 'r')

    # For future save_to file
    # output_file = False
    # output_file = open(argv.o, 'w')

    v = argv.version
    print("version {}".format(v))

    rna_object = FullRNA(input_file, v=v)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Process some integers.')
    parser.add_argument('input_file')
    # parser.add_argument('-o', default=False)
    parser.add_argument('-v', '--version', type=int, default=1, choices=[0, 1])
    args = parser.parse_args()
    main(args)
