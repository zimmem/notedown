package com.zimmem.notedown.evernote;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.evernote.edam.type.Note;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class ObjectMapperFactory {

    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter("setPropertyFilter",
                                                                            new SimpleBeanPropertyFilter() {

                                                                                @Override
                                                                                protected boolean include(PropertyWriter writer) {
                                                                                    System.out.println(writer.getName());
                                                                                    return !writer.getName().startsWith("set");
                                                                                }

                                                                                @Override
                                                                                protected boolean include(BeanPropertyWriter writer) {
                                                                                    System.out.println(writer.getName());
                                                                                    return !writer.getName().startsWith("set");
                                                                                }
                                                                            });
        // FilterProvider filters = new SimpleFilterProvider()
        // .addFilter("myFilter", SimpleBeanPropertyFilter
        // .filterOutAllExcept(new HashSet<String>(Arrays
        // .asList(new String[] { "name", "firstName" , "setUpdateSequenceNum","guid"}))));
        mapper.setFilters(filters);
        return mapper;
    }
    
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = createObjectMapper();
        String s = mapper.writeValueAsString(new MyNote());
        System.out.println(s);
    }
    
    @JsonFilter("setPropertyFilter")
    public static class MyNote extends Note{
        
    }
}
