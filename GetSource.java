Class GetSource{

	public void GetSource(String args[]){
		Map queryArgs = new HashMap();
		queryArgs.put("name",args[0]);
		queryArgs.put("FWBS",args[1]);
		queryArgs.put("FWID",args[2]);
		queryArgs.put("QUERY",args[3]);
		qqbw = "new"+ queryArgs.get("name")+queryArgs.get("FWBS")+queryArgs.get("FWID")++queryArgs.get("QUERY");
	}

	public void GetSource(String args[],Boolean old){
		Map queryArgs = new HashMap();
		queryArgs.put("name",args[0]);
		queryArgs.put("FWBS",args[1]);
		queryArgs.put("FWID",args[2]);
		queryArgs.put("QUERY",args[3]);
		qqbw = "old"+ queryArgs.get("name")+queryArgs.get("FWBS")+queryArgs.get("FWID")++queryArgs.get("QUERY");
	}
}