# elgoog

**elgoog** is a (basic) document-search and query-retrieval engine that I developed for the [Applied Information Retrieval (CS546, Fall-2019)](http://ciir.cs.umass.edu/~dfisher/cs546/) class at [UMass, Amherst](https://www.cics.umass.edu/).

**elgoog** uses [inverted-index](https://github.com/sg1993/elgoog/blob/master/elgoog/src/index/InvertedFileIndex.java) for fast query lookup.

It also supports phrase operators (like [ordered](https://github.com/sg1993/elgoog/blob/master/elgoog/src/retriever/inferencenetwork/OrderedWindowProximityNode.java) and [unordered](https://github.com/sg1993/elgoog/blob/master/elgoog/src/retriever/inferencenetwork/UnorderedWindowProximityNode.java) windows or exact phrase matches (which is same as ordered window operator with no separation between the terms)) by using a [Bayesian inference network model](https://github.com/sg1993/elgoog/tree/master/elgoog/src/retriever/inferencenetwork) for query retrieval.

**elgoog** currently uses 4 scoring mechanisms: 
1) raw-count
2) [BM-25](https://github.com/sg1993/elgoog/blob/master/elgoog/src/retriever/evaluation/BM25Evaluator.java)
3) Query likelihood using [Jelenik Mercer smoothing](https://github.com/sg1993/elgoog/blob/master/elgoog/src/retriever/evaluation/JelinekMercerEvaluator.java)
4) Query likelihood using [Dirichlet smoothing](https://github.com/sg1993/elgoog/blob/master/elgoog/src/retriever/evaluation/DirichletEvaluator.java)
