package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

import java.time.LocalDate;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactRepository contactRepository;

    // BEGIN
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO postContact (ContactCreateDTO contact) {
        var contactEntity = new Contact();
        contactEntity.setFirstName(contact.getFirstName());
        contactEntity.setLastName(contact.getLastName());
        contactEntity.setPhone(contact.getPhone());
//        contactEntity.setCreatedAt(LocalDate.now());
//        contactEntity.setUpdatedAt(LocalDate.now());
        var result = contactRepository.save(contactEntity);
        var returnContact = new ContactDTO();
        returnContact.setId(result.getId());
        returnContact.setFirstName(result.getFirstName());
        returnContact.setLastName(result.getLastName());
        returnContact.setPhone(result.getPhone());
        returnContact.setCreatedAt(result.getCreatedAt());
        returnContact.setUpdatedAt(result.getUpdatedAt());
        return returnContact;
    }
    // END
}
