package com.restaurant.service.impl;

import com.restaurant.entity.MenuItem;
import com.restaurant.repository.MenuItemRepository;
import com.restaurant.service.MenuItemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static com.restaurant.util.TestUtil.TEST_MENU_ITEM_ID;
import static com.restaurant.util.TestUtil.TEST_MENU_ITEM_NAME;
import static com.restaurant.util.TestUtil.getMenuItem;
import static com.restaurant.util.TestUtil.getMenuItemDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.openMocks;

public class MenuItemServiceImplTest {
    @Mock
    private MenuItemRepository menuItemRepository;

    private MenuItemService menuItemService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        menuItemService = new MenuItemServiceImpl(menuItemRepository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testSuccessfulAddMenuItem() {
        menuItemService.addMenuItem(getMenuItemDTO());

        verify(menuItemRepository).save(any(MenuItem.class));
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    public void testSuccessfulDeleteMenuItem() {
        MenuItem menuItem = getMenuItem();
        doReturn(Optional.of(menuItem))
                .when(menuItemRepository)
                .findById(any());

        menuItemService.deleteMenuItem(TEST_MENU_ITEM_ID);

        verify(menuItemRepository).findById(TEST_MENU_ITEM_ID);
        verify(menuItemRepository).delete(menuItem);
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    public void testFailureDeleteMenuItemDueToNotFound() {
        doReturn(Optional.empty())
                .when(menuItemRepository)
                .findById(any());

        try {
            menuItemService.deleteMenuItem(TEST_MENU_ITEM_ID);
            fail("Expected RuntimeException not thrown");
        } catch (Exception e) {
            assertInstanceOf(RuntimeException.class, e);
            assertEquals("Menu item not found", e.getMessage());
        }

        verify(menuItemRepository).findById(TEST_MENU_ITEM_ID);
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    public void testSuccessfulGetAllMenuItems() {
        menuItemService.getAllMenuItems();

        verify(menuItemRepository).findAll();
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    public void testSuccessfulUpdateMenuItem() {
        MenuItem menuItem = getMenuItem();
        doReturn(Optional.of(menuItem))
                .when(menuItemRepository)
                .findByName(any());

        menuItemService.updateMenuItem(getMenuItemDTO());

        verify(menuItemRepository).findByName(TEST_MENU_ITEM_NAME);
        verify(menuItemRepository).save(menuItem);
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    public void testFailureUpdateMenuItemDueToNotFound() {
    doReturn(Optional.empty())
            .when(menuItemRepository)
            .findByName(any());

        try {
            menuItemService.updateMenuItem(getMenuItemDTO());
            fail("Expected RuntimeException not thrown");
        } catch (Exception e) {
            assertInstanceOf(RuntimeException.class, e);
            assertEquals("Menu item not found", e.getMessage());
        }

        verify(menuItemRepository).findByName(TEST_MENU_ITEM_NAME);
        verifyNoMoreInteractions(menuItemRepository);
    }
}
