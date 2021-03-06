package org.basex.query.iter;

import org.basex.query.*;
import org.basex.query.value.*;
import org.basex.query.value.item.*;
import org.basex.query.value.seq.*;

/**
 * Iterator interface.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public abstract class Iter {
  /**
   * Returns the next item or {@code null} if no other items are found.
   * @return resulting item or {@code null}
   * @throws QueryException query exception
   */
  public abstract Item next() throws QueryException;

  /**
   * Returns the specified item, or an arbitrary item if the index is invalid.
   * If this method is implemented by an iterator, {@link #size()} needs to be implemented as well.
   * @param i value offset
   * @return specified item
   * @throws QueryException query exception
   */
  @SuppressWarnings("unused")
  public Item get(final long i) throws QueryException {
    return null;
  }

  /**
   * Returns the iterator size. Note: {@code -1} is returned if the result size is unknown. If this
   * method is implemented by an iterator, {@link #get(long)} needs to be implemented as well.
   * @return number of entries
   */
  public long size() {
    return -1;
  }

  /**
   * Returns a value with all iterated items.
   * Must only be called if {@link #next()} has not been called before.
   * @param qc query context
   * @return sequence
   * @throws QueryException query exception
   */
  public Value value(final QueryContext qc) throws QueryException {
    // check if sequence is empty
    final Item i1 = next();
    if(i1 == null) return Empty.SEQ;

    // check for single result
    final Item i2 = next();
    if(i2 == null) return i1;

    // more results: build sequence
    final ValueBuilder vb = new ValueBuilder().add(i1).add(i2);
    for(Item it; (it = next()) != null;) {
      qc.checkStop();
      vb.add(it);
    }
    return vb.value();
  }
}
