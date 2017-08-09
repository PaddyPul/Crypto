public boolean isValidTx(Transaction tx) {
	    
		ArrayList<UTXO> seenUTXO = new ArrayList<UTXO>();
		
		double inSum = 0;
		double outSum = 0;
		
		int index = 0;

		for (Transaction.Input in : tx.getInputs()) {
			
			UTXO checkUTXO = new UTXO(in.prevTxHash, in.outputIndex);
			if (seenUTXO.contains(checkUTXO)) return false; // 3
			//no UTXO is claimed multiple times by tx
			
			seenUTXO.add(checkUTXO);
			
			//if the transaction pool doesn't contain it already
			if (!up.contains(checkUTXO)) return false; // 1
			
			inSum += up.getTxOutput(checkUTXO).value;
			
			// Check Signature
			if (!up.getTxOutput(checkUTXO).address.verifySignature(tx.getRawDataToSign(index), in.signature)) return false; // 2
			
			index++;
		}
		
		for (Transaction.Output out : tx.getOutputs()) {
			if (out.value < 0) return false; // 4
			outSum += out.value;
		}
		
		if (outSum > inSum) return false; // 5
		
		return true;
	}