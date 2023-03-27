from mido import Message

from notes import get_note, get_keys


class GetMelody:
    def __init__(self, mid):
        self.duration = 0
        self.notes = []
        self.parse_msg(mid)
        self.notes_amount = len(self.notes)
        self.velocity = (mid.tracks[1])[2].velocity
        self.tempo = (mid.tracks[0])[1].tempo
        self.instrument = (mid.tracks[1])[0].name
        self.keys = get_keys([note['name'][:-1] for note in self.notes])

    def parse_msg(self, mid) -> None:
        on = 0
        for m in mid.tracks[1]:
            if type(m) is not Message or m.type == 'program_change': continue
            if m.type == 'note_off':
                self.duration += on + m.time
                self.notes.append({'name': get_note(m.note), 'id': m.note,
                                   'on': self.duration - m.time, 'off': self.duration})
            else:
                on = m.time
