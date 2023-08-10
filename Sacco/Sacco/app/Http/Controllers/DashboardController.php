<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Failed_Login;
use App\Models\Failed_Deposits;
use App\Models\Loan_Requests;

class DashboardController extends Controller
{
    public function index()
    {
        $records = $this->failed();
        $rows = $this->deposits();
        $lines = $this->requests();

        return view('dashboard.index', [
            'records' => $records,
            'rows' => $rows,
            'lines' => $lines,
        ]);
    }

    public function failed()
    {
        $records = Failed_Login::all();
        return $records;
    }

    public function deposits()
    {
        $rows = Failed_Deposits::all();
        return $rows;
    }

    public function requests()
    {
        $lines = Loan_Requests::all();
        return $lines;
    }
}
