package uk.co.magictractor.zillions.core.importer;

import uk.co.magictractor.zillions.core.BigInt;

public interface ByteImporter {

	/**
	 * Bytes should be in <i>big-endian</i> byte-order: the most significant
     * byte is in the zeroth element.
	 */
	BigInt from(BigInt rop, byte[] bytes);
	
}