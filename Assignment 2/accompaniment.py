from mido import MidiTrack, MetaMessage

class GetAccompaniment:
    def __init__(self, melody):
        # MetaMessage('end_of_track', time=0)
        self.track = MidiTrack([MetaMessage('track_name', name=melody.instrument, time=0)])

    def insert(self, mid) -> None:
        mid.tracks.append(self.track)
