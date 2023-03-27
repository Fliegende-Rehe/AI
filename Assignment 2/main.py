from mido import MidiFile

from accompaniment import GetAccompaniment
from melody import GetMelody


def main(n):
    mid = MidiFile(f'inputs/input{n}.mid')
    melody = GetMelody(mid)
    print(melody.keys)
    accompaniment = GetAccompaniment(melody)
    accompaniment.insert(mid)
    # mid.save(f'outputs/RuslanAbdullinOutput{n}-{melody.key}.mid')
    print()

if __name__ == '__main__':
    # main(1)
    for i in range(1, 4):
        main(i)
