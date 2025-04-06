import unittest

import constants as cte
from mutation_fuzzer import sadd, sdel, srpl, cadd, radd, cdel, rdel, mrpl


class TestActionMutators(unittest.TestCase):

    def test_sadd(self):
        self.assertEqual(len(sadd("",   cte.ACTIONS)), 1)
        self.assertEqual(len(sadd("E",  cte.ACTIONS)), 2)
        self.assertEqual(len(sadd("UE", cte.ACTIONS)), 3)

    def test_sdel(self):
        self.assertEqual(len(sdel("",   cte.ACTIONS)), 0)
        self.assertEqual(len(sdel("E",  cte.ACTIONS)), 0)
        self.assertEqual(len(sdel("UE", cte.ACTIONS)), 1)

    def test_sdel(self):
        self.assertEqual(len(srpl("",   cte.ACTIONS)), 0)
        self.assertEqual(len(srpl("E",  cte.ACTIONS)), 1)
        self.assertEqual(len(srpl("UE", cte.ACTIONS)), 2)


class TestMapMutators(unittest.TestCase):

    def test_radd(self):
        # Adding a row to an empty map produces a 1x1 map.
        map_ = []
        map_ = radd(map_, cte.CELLS)
        self.assertEqual(len(map_), 1)
        self.assertEqual(len(map_[0]), 1)

        # Adding a row to a 1x1 map produces a 1x2 map.
        map_ = ["0"]
        map_ = radd(map_, cte.CELLS)
        # There are two rows.
        self.assertEqual(len(map_), 2)
        # Each row is of length 1.
        self.assertEqual([len(row) for row in map_], [1, 1])

        # Adding a row to a nxm map produces a nx(m+1) map.
        map_ = ["0WPF", "0WPF", "0WPF"]
        map_ = radd(map_, cte.CELLS)
        self.assertEqual(len(map_), 4)
        self.assertEqual([len(row) for row in map_], [4, 4, 4, 4])

        # Adding a row to a 4x1 map produces a 5x1 map.
        map_ = ["W", "W", "W", "W"]
        map_ = radd(map_, cte.CELLS)
        self.assertEqual(len(map_), 5)
        self.assertEqual([len(row) for row in map_], [1, 1, 1, 1, 1])

    def test_cadd(self):
        # Adding a col to an empty map produces a 1x1 map.
        map_ = []
        map_ = cadd(map_, cte.CELLS)
        self.assertEqual(len(map_), 1)
        self.assertEqual(len(map_[0]), 1)

        # Adding a col to a 1x1 map produces a 2x1 map.
        map_ = ["0"]
        map_ = cadd(map_, cte.CELLS)
        # There is one row.
        self.assertEqual(len(map_), 1)
        # Each row is of length 2.
        self.assertEqual([len(row) for row in map_], [2])

        # Adding a row to a nxm map produces a nx(m+1) map.
        map_ = ["0WPF", "0WPF", "0WPF"]
        map_ = cadd(map_, cte.CELLS)
        self.assertEqual(len(map_), 3)
        self.assertEqual([len(row) for row in map_], [5, 5, 5])

        # Adding a col to a 4x1 map produces a 4x2 map.
        map_ = ["W", "W", "W", "W"]
        map_ = cadd(map_, cte.CELLS)
        self.assertEqual(len(map_), 4)
        self.assertEqual([len(row) for row in map_], [2, 2, 2, 2])

    def test_rdel(self):
        # Deleting a row from an empty map produces a 0x0 map.
        map_ = []
        map_ = rdel(map_, cte.CELLS)
        self.assertEqual(len(map_), 0)

        # Deleting a row from a 1x1 map produces a 0x0 map.
        map_ = ["0"]
        map_ = rdel(map_, cte.CELLS)
        self.assertEqual(len(map_), 0)

        # Deleting a row from a nxm map produces a nx(m-1) map.
        map_ = ["0WPF", "0WPF", "0WPF"]
        map_ = rdel(map_, cte.CELLS)
        # There are two rows.
        self.assertEqual(len(map_), 2)
        # Each row is of length 4.
        self.assertEqual([len(row) for row in map_], [4, 4])

        # Adding a row to a 4x1 map produces a 3x1 map.
        map_ = ["W", "W", "W", "W"]
        map_ = rdel(map_, cte.CELLS)
        self.assertEqual(len(map_), 3)
        self.assertEqual([len(row) for row in map_], [1, 1, 1])

    def test_cdel(self):
        # Deleting a col from an empty map produces a 0x0 map.
        map_ = []
        map_ = cdel(map_, cte.CELLS)
        self.assertEqual(len(map_), 0)

        # Deleting a col from a 1x1 map produces a 0x0 map.
        map_ = ["0"]
        map_ = cdel(map_, cte.CELLS)
        self.assertEqual(len(map_), 0)

        # Deleting a col from a nxm map produces a (n-1)xm map.
        map_ = ["0WPF", "0WPF", "0WPF"]
        map_ = cdel(map_, cte.CELLS)
        # There are three rows.
        self.assertEqual(len(map_), 3)
        # Each row is of length 3.
        self.assertEqual([len(row) for row in map_], [3, 3, 3])

        # Deleting a row from a 4x1 map produces a 0x0 map.
        map_ = ["W", "W", "W", "W"]
        map_ = cdel(map_, cte.CELLS)
        self.assertEqual(len(map_), 0)

    def test_mrpl(self):
        # Replacing a cell in an empty map produces a 0x0 map.
        map_ = []
        map_ = mrpl(map_, cte.CELLS)
        self.assertEqual(len(map_), 0)

        # Replacing a cell in a 1x1 map produces a 1x1 map.
        map_ = ["0"]
        map_ = mrpl(map_, cte.CELLS)
        self.assertEqual(len(map_), 1)

        # Replacing a cell in a nxm map produces a nxm map.
        map_ = ["0WPF", "0WPF", "0WPF"]
        map_ = mrpl(map_, cte.CELLS)
        # There are three rows.
        self.assertEqual(len(map_), 3)
        # Each row is of length 4.
        self.assertEqual([len(row) for row in map_], [4, 4, 4])

        # Replacing a cell in a 4x1 map produces a 4x1 map.
        map_ = ["W", "W", "W", "W"]
        map_ = mrpl(map_, cte.CELLS)
        self.assertEqual(len(map_), 4)
        self.assertEqual([len(row) for row in map_], [1, 1, 1, 1])



if __name__ == '__main__':
    unittest.main()
