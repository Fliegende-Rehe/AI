OCTAVE = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#']


def get_note(n):
    if isinstance(n, int): return OCTAVE[(n - 21) % 12] + str(int(n / 12) - 1)
    return (int(n[-1]) + 1) * 12 + OCTAVE.index(n[:-1]) - 3


def get_keys(notes):
    unique_notes = set(notes)
    print(unique_notes)
    keys_table = create_keys_table()
    keys = []
    for k in keys_table:
        flag = True
        for n in unique_notes:
            if n not in k:
                flag = False
                break
        if flag:
            print(k)
            keys.append(k[0] + ('M' if keys_table.index(k) < 12 else 'm'))  # key[0] - tonic
    return keys


# keys table generator
def create_keys_table():
    # major + minor scale
    scale = [[2, 2, 1, 2, 2, 2], [2, 1, 2, 2, 1, 2]]
    table = []
    for sc in scale:
        for note in OCTAVE:
            key = [note]
            j = OCTAVE.index(note)
            for i in sc:
                key.append(OCTAVE[j + i if j + i < 12 else i + j - 12])
                j += i
            table.append(key)
    return table
