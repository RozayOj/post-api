package com.reckue.post.services.realizations;

import com.reckue.post.PostServiceApplicationTests;
import com.reckue.post.exceptions.ModelAlreadyExistsException;
import com.reckue.post.exceptions.ModelNotFoundException;
import com.reckue.post.models.PollNode;
import com.reckue.post.models.Tag;
import com.reckue.post.repositories.PollNodeRepository;
import com.reckue.post.utils.Generator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Class PollNodeServiceRealizationTest represents test for PollNodeService class.
 *
 * @author Viktor Grigoriev
 */
public class PollNodeServiceRealizationTest extends PostServiceApplicationTests {

    @Mock
    private PollNodeRepository pollNodeRepository;

    @InjectMocks
    private PollNodeServiceRealization pollNodeService;

    @Test
    public void create() {
        String ID = Generator.id();
        PollNode node = PollNode.builder().id(ID).title("title").build();
        when(pollNodeRepository.save(node)).thenReturn(node);

        assertEquals(node, pollNodeService.create(node));
    }

    @Test
    public void createIfPollAlreadyExist() {
        PollNode node = PollNode.builder().id("1").title("title").build();

        doReturn(true).when(pollNodeRepository).existsById(Mockito.anyString());

        Exception exception = assertThrows(ModelAlreadyExistsException.class, () -> pollNodeService.create(node));
        assertEquals("PollNode already exists", exception.getMessage());
    }

    @Test
    public void update() {
        PollNode node = PollNode.builder().id("1").title("title").build();

        when(pollNodeRepository.existsById(node.getId())).thenReturn(true);
        when(pollNodeRepository.save(node)).thenReturn(node);

        Assertions.assertEquals(node, pollNodeService.update(node));
    }

    @Test
    public void updateTagWithNullId() {
        PollNode node = PollNode.builder().build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> pollNodeService.update(node));
        assertEquals("The parameter is null", exception.getMessage());
    }

    @Test
    public void updateTagIfNotExistId() {
        PollNode node = PollNode.builder().id("1").title("title").build();
        when(pollNodeRepository.existsById(node.getId())).thenReturn(false);

        Exception exception = assertThrows(ModelNotFoundException.class, () -> pollNodeService.update(node));
        assertEquals("PollNode by id " + node.getId() + " is not found", exception.getMessage());
    }

    @Test
    public void findById() {
        PollNode node = PollNode.builder().id("wild").title("title").build();
        doReturn(Optional.of(node)).when(pollNodeRepository).findById(Mockito.anyString());

        assertEquals(node, pollNodeService.findById(node.getId()));
    }

    @Test
    public void findByIdIfNotExist() {
        PollNode node = PollNode.builder().id("saturn").title("title").build();

        Exception exception = assertThrows(ModelNotFoundException.class, () -> pollNodeService.findById(node.getId()));
        assertEquals("PollNode by id " + node.getId() + " is not found", exception.getMessage());
    }

    @Test
    public void findAll() {
        PollNode node = PollNode.builder().id("1").title("title").build();
        PollNode nodeTwo = PollNode.builder().id("2").title("titleTwo").build();

        List<PollNode> nodes = Stream.of(node, nodeTwo).collect(Collectors.toList());

        when(pollNodeRepository.findAll()).thenReturn(nodes);
        assertEquals(nodes, pollNodeService.findAll());
    }

    @Test
    public void findAllAndSortByName() {
        PollNode nodeOne = PollNode.builder().id("b1").title("titleOne").build();
        PollNode nodeTwo = PollNode.builder().id("10").title("titleTwo").build();
        PollNode nodeThree = PollNode.builder().id("999").title("titleThree").build();

        List<PollNode> nodes = Stream.of(nodeOne, nodeTwo, nodeThree).collect(Collectors.toList());
        when(pollNodeRepository.findAll()).thenReturn(nodes);

        List<PollNode> expected = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getTitle))
                .collect(Collectors.toList());

        assertNotEquals(nodes, pollNodeService.findAllAndSortByTitle());
        assertEquals(expected, pollNodeService.findAllAndSortByTitle());
    }

    @Test
    public void findAllAndSortById() {
        PollNode nodeOne = PollNode.builder().id("b2").title("titleOne").build();
        PollNode nodeTwo = PollNode.builder().id("7up").title("titleTwo").build();
        PollNode nodeThree = PollNode.builder().id("777").title("titleThree").build();

        List<PollNode> nodes = Stream.of(nodeOne, nodeTwo, nodeThree).collect(Collectors.toList());
        when(pollNodeRepository.findAll()).thenReturn(nodes);

        List<PollNode> expected = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getId))
                .collect(Collectors.toList());

        assertNotEquals(nodes, pollNodeService.findAllAndSortById());
        assertEquals(expected, pollNodeService.findAllAndSortById());
    }

    @Test
    public void findAllBySortType() {
        PollNode nodeOne = PollNode.builder().id("xxx").title("titleOne").build();
        PollNode nodeTwo = PollNode.builder().id("11").title("titleTwo").build();
        PollNode nodeThree = PollNode.builder().id("woodoo").title("titleThree").build();

        List<PollNode> nodes = Stream.of(nodeOne, nodeTwo, nodeThree).collect(Collectors.toList());
        when(pollNodeRepository.findAll()).thenReturn(nodes);

        List<PollNode> sortedByNameExpected = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getTitle))
                .collect(Collectors.toList());

        List<PollNode> sortedByIdExpected = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getId))
                .collect(Collectors.toList());

        assertEquals(sortedByNameExpected, pollNodeService.findAllBySortType("title"));
        assertEquals(sortedByIdExpected, pollNodeService.findAllBySortType("id"));
    }

    @Test
    public void findAllByTypeAndDesc() {
        PollNode nodeOne = PollNode.builder().id("eleven").title("titleOne").build();
        PollNode nodeTwo = PollNode.builder().id("112358").title("titleTwo").build();
        PollNode nodeThree = PollNode.builder().id("b8").title("titleThree").build();

        List<PollNode> nodes = Stream.of(nodeOne, nodeTwo, nodeThree).collect(Collectors.toList());
        when(pollNodeRepository.findAll()).thenReturn(nodes);

        List<PollNode> sortedByNameExpected = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getTitle))
                .collect(Collectors.toList());

        List<PollNode> sortedByIdExpected = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getId))
                .collect(Collectors.toList());

        List<PollNode> sortedByNameDescExpected = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getTitle).reversed())
                .collect(Collectors.toList());

        List<PollNode> sortedByIdDescExpected = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getId).reversed())
                .collect(Collectors.toList());

        assertEquals(sortedByNameExpected, pollNodeService.findAllByTypeAndDesc("title", false));
        assertEquals(sortedByIdExpected, pollNodeService.findAllByTypeAndDesc("id", false));
        assertEquals(sortedByNameDescExpected, pollNodeService.findAllByTypeAndDesc("title", true));
        assertEquals(sortedByIdDescExpected, pollNodeService.findAllByTypeAndDesc("id", true));
    }

    @Test
    public void findAllWithLimitOffsetSortAndDesc() {
        PollNode nodeOne = PollNode.builder().id("3").title("a").build();
        PollNode nodeTwo = PollNode.builder().id("1").title("c").build();
        PollNode nodeThree = PollNode.builder().id("4").title("b").build();
        PollNode nodeFour = PollNode.builder().id("2").title("d").build();

        List<PollNode> nodes = Stream.of(nodeOne, nodeTwo, nodeThree, nodeFour).collect(Collectors.toList());
        when(pollNodeRepository.findAll()).thenReturn(nodes);

        List<PollNode> test1 = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getTitle))
                .limit(2)
                .skip(1)
                .collect(Collectors.toList());

        List<PollNode> test2 = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getTitle).reversed())
                .limit(3)
                .collect(Collectors.toList());

        List<PollNode> test3 = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getId))
                .limit(1)
                .skip(2)
                .collect(Collectors.toList());

        List<PollNode> test4 = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getId))
                .limit(2)
                .skip(1)
                .collect(Collectors.toList());

        List<PollNode> test5 = nodes.stream()
                .sorted(Comparator.comparing(PollNode::getId).reversed())
                .limit(2)
                .skip(1)
                .collect(Collectors.toList());

        assertEquals(test1, pollNodeService.findAll(2, 1, "title", false));
        assertEquals(test2, pollNodeService.findAll(3, 0, "title", true));
        assertEquals(test3, pollNodeService.findAll(1, 2, "id", false));
        assertEquals(test4, pollNodeService.findAll(2, 1, "id", false));
        assertEquals(test5, pollNodeService.findAll(2, 1, "id", true));
    }

    @Test
    public void deleteById() {
        PollNode node = PollNode.builder()
                .id("1")
                .title("title")
                .build();
        List<PollNode> nodes = new ArrayList<>();
        nodes.add(node);

        when(pollNodeRepository.existsById(node.getId())).thenReturn(true);
        doAnswer(invocation -> {
            nodes.remove(node);
            return null;
        }).when(pollNodeRepository).deleteById(node.getId());
        pollNodeRepository.deleteById(node.getId());

        assertEquals(0, nodes.size());
    }

    @Test
    public void deleteByIdWithException() {
        PollNode node = PollNode.builder().id("0").title("title").build();

        Exception exception = assertThrows(
                ModelNotFoundException.class, () -> pollNodeService.deleteById(node.getId()));
        assertEquals("PollNode by id " + node.getId() + " is not found", exception.getMessage());
    }
}