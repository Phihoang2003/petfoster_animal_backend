package com.hoangphi.service.impl.search_history;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.entity.SearchHistory;
import com.hoangphi.entity.User;
import com.hoangphi.repository.SearchHistoryRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.search_history.SearchHistoryResponse;
import com.hoangphi.service.search_history.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    @Override
    public ApiResponse getSearchHistory(String jwt) {
        Map<String, String> errorsMap = new HashMap<>();
        User user = userRepository.findByUsername(jwtProvider.getUsernameFromToken(jwt)).orElse(null);

        if (user == null) {
            errorsMap.put("user", "user not found");
            return ApiResponse.builder()
                    .message("Unauthenrized")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .errors(errorsMap).build();
        }

        List<SearchHistoryResponse> listResponse = getListResponse(user.getId());

        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(null)
                .data(listResponse)
                .build();
    }

    @Override
    public ApiResponse updateSearchHistory(String jwt, String keyword) {
        Map<String, String> errorsMap = new HashMap<>();
        User user = userRepository.findByUsername(jwtProvider.getUsernameFromToken(jwt)).orElse(null);
        if (user == null) {
            errorsMap.put("user", "user not found");
            return ApiResponse.builder()
                    .message("Unauthenticated")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .errors(errorsMap)
                    .build();
        }
        if (keyword.isBlank()) {
            return ApiResponse.builder()
                    .message("Query product Successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .errors(true)
                    .data(getListResponse(user.getId()))
                    .build();
        }
        List<SearchHistory> listSearch=searchHistoryRepository.findByUserId(user.getId()).orElse(null);
        SearchHistory newSearch=SearchHistory.builder()
                .keyword(keyword)
                .user(user)
                .build();

        assert listSearch != null;
        if(listSearch.size()==5){
            searchHistoryRepository.delete(listSearch.get(4));
            searchHistoryRepository.save(newSearch);
        }
        listSearch.forEach(itemHistory->{
            if(itemHistory.getKeyword().equalsIgnoreCase(keyword)){
                searchHistoryRepository.delete(itemHistory);
                searchHistoryRepository.save(newSearch);
            }
        });
        searchHistoryRepository.save(newSearch);
        List<SearchHistoryResponse> listResponse = getListResponse(user.getId());

        return ApiResponse.builder()
                .message("Query product Successfully")
                .status(HttpStatus.OK.value())
                .errors(null)
                .data(listResponse)
                .build();
    }

    @Override
    public ApiResponse deleteSearchHistory(String jwt, String keyword) {
        return null;
    }

    public List<SearchHistoryResponse>  getListResponse(String userId){
        List<SearchHistory> listSearch=searchHistoryRepository.findByUserId(userId).orElse(null);
        assert listSearch != null;
        return listSearch.stream().map(item->{
            return new SearchHistoryResponse(item.getId(),item.getKeyword());
        }).toList();
    }
}
