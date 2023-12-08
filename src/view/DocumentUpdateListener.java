package view;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This abstract class represents a document update listener that implements the DocumentListener interface.
 * We use it as a convenient jtextinput update handler
 * When the associated text input is updated, the {@link #update()} method will be called.
 * Subclasses must implement the {@link #update()} method to define the custom behavior for document updates.
 */
public abstract class DocumentUpdateListener implements DocumentListener {
  @Override
  public void insertUpdate(DocumentEvent e) {
    update();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    update();
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    update();
  }

  protected abstract void update();
}
