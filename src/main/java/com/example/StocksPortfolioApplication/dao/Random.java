package com.example.StocksPortfolioApplication.dao;

public class Random {
//    Integer findSum(List<WholeTransactionDetailsDto> list){
//        if(list.isEmpty()){
//            return 0;
//        }
//        Integer sum=0;
//
//        for(WholeTransactionDetailsDto L:list){
//            sum+=L.getQuantity();
//        }
//
//        return sum;
//    }
//
//    Double  findAvgPrice(List<WholeTransactionDetailsDto>list,Integer totalQuantity){
//        if(list.isEmpty()){
//            return 0.0;
//        }
//        Double totalSum=0.0;
//        for(WholeTransactionDetailsDto L:list){
//            totalSum+=L.getCostPerStock()*L.getQuantity();
//        }
//
//        return totalSum/totalQuantity;
//
//    }
//    void generateTransactionDetails( Map<UniqueTransactionDto,List<WholeTransactionDetailsDto>> gettingWholeDetails, HashSet<Integer> uniqueStockid,List<UserTransactionDetails> listOfUsersTransactionDetails){
//        for(UserTransactionDetails userTransactionDetails:listOfUsersTransactionDetails){
//            UniqueTransactionDto currentUniqueTransactionDto=new UniqueTransactionDto(userTransactionDetails.getUserAccountId(),userTransactionDetails.getStockId(),userTransactionDetails.getTransactionType());
//            if(gettingWholeDetails.containsKey(currentUniqueTransactionDto)){
//                gettingWholeDetails.get(currentUniqueTransactionDto).add(new WholeTransactionDetailsDto(userTransactionDetails.getQuantity(),userTransactionDetails.getStockPrice()));
//            }
//            else{
//                List<WholeTransactionDetailsDto> intialList=new ArrayList<>();
//                intialList.add(new WholeTransactionDetailsDto(userTransactionDetails.getQuantity(),userTransactionDetails.getStockPrice()));
//                gettingWholeDetails.put(currentUniqueTransactionDto,intialList);
//            }
//            uniqueStockid.add(userTransactionDetails.getStockId());
//
//        }
//
//    }

}
