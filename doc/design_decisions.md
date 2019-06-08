== BigInt method names and behaviour

BigInt's method names and behaviour are based on Java's BigInteger class.

This satisfies the "principle of least astonishment". Java developers familiar with BigInteger should find BigInt easy to use.

Having the same behaviour means that a reference implementation of BigInt which wraps BigInteger is very simple and easy to maintain. When new features are added to BigInt, the test suite is usually run again the BigInteger implementation first and then other BigInt implementations are added.

Examples where BigInt implementations have made adjustments to standardise behaviour include the bit length and bit count implementations for negative values.


== BigInt mutability

Unlike Java's BigInteger class, BigInt is mutable.

This is because high performance arithmetic libraries written in C/C++ and wrapped by BigInt are likely to have mutable values. GmpLib is an example of this.

Many operations on BigInt return a BigInt, but those should all return the instance being operated on (return this). This allows operations on BigInts to be chained.


== Avoid long in APIs

longs have been avoided in APIs. Where it might have been possible to use a long, an int is used.

This is primarily because longs in C libraries are on Windows generally by default compiled to 4 bytes, and on other platforms are compiled to 8 bytes.

For example BigInt.add(int), BigInt.subtract(int), BigInt.multiply(int).
