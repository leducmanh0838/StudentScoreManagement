import { useEffect, useState } from 'react';
import Apis, { authApis } from './Apis';

const PaginatedData = (endpoint, params = {}, useAuth = false) => {
  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);

  const httpClient = useAuth ? authApis() : Apis;

  const buildUrl = (pageNumber) => {
    let url = `${endpoint}?page=${pageNumber}`;
    for (const key in params) {
      if (params[key]) {
        url += `&${key}=${params[key]}`;
      }
    }
    console.info(url);
    return url;
  };

  const loadItems = async (pageNumber = 1, reset = false) => {
    setLoading(true);
    try {
      const url = buildUrl(pageNumber);
      const res = await httpClient.get(url);
      const result = res.data;

      if (!result || result.length === 0) {
        setHasMore(false);
        return;
      }

      if (reset) {
        setData(result);
      } else {
        setData(prev => [...prev, ...result]);
      }

      setPage(pageNumber + 1);
    } catch (error) {
      console.error("Load error:", error);
    } finally {
      setLoading(false);
    }
  };

  const loadMore = () => {
    if (!loading && hasMore) {
      loadItems(page);
    }
  };

  const refresh = () => {
    setHasMore(true);
    setPage(1);
    loadItems(1, true);
  };

  useEffect(() => {
    refresh();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [JSON.stringify(params)]);

  return { data, setData, loading, loadMore, refresh, hasMore };
};

export default PaginatedData;
