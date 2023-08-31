package ru.codeinside.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NoteVersApplicationTests {

  private final static String INIT_NOTE_DATA = "Lorem ipsum dolor";
  private final static String UPDATE_NOTE_DATA = "Lorem ipsum dolor sit amet";
  @Autowired
  NoteService noteService;
  @Autowired
  private NoteRepository noteRepository;

  @Test
  void contextLoads() {
  }

  @Test
  void saveAndUpdateTest() {
    var createdNote = noteRepository.save(Note.builder()
        .data(INIT_NOTE_DATA)
        .build());

    assertEquals(INIT_NOTE_DATA, noteRepository.findById(createdNote.getId()).map(Note::getData).orElse(null));

    createdNote.setData(UPDATE_NOTE_DATA);
    var updatedNote = noteRepository.save(createdNote);

    assertEquals(UPDATE_NOTE_DATA, updatedNote.getData());
    assertEquals(createdNote.getCreateDate(), updatedNote.getCreateDate());

    var versions = noteService.getAllVersions(updatedNote.getId());
    assertEquals(2, versions.size());

    var firstNoteVersion = versions.get(0);
    var secondNoteVersion = versions.get(1);

    assertEquals(createdNote.getId(), firstNoteVersion.getId());
    assertEquals(createdNote.getId(), secondNoteVersion.getId());
    assertEquals(INIT_NOTE_DATA, firstNoteVersion.getData());
    assertEquals(UPDATE_NOTE_DATA, secondNoteVersion.getData());

    assertEquals(createdNote.getCreateDate(), firstNoteVersion.getCreateDate());
    assertEquals(createdNote.getCreateDate(), secondNoteVersion.getCreateDate());
    assertEquals(updatedNote.getUpdateDate(), secondNoteVersion.getUpdateDate());
  }

}
