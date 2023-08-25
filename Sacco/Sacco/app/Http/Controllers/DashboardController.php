<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Failed_Login;
use App\Models\Failed_Deposits;
use App\Models\Loan_Requests;
use App\Models\Members;
use App\Models\Registered_Loans;
use App\Models\Deposits;

class DashboardController extends Controller
{
    public function index()
    {
        $records = $this->failed();
        $rows = $this->deposits();
        $lines = $this->requests();
        $members = $this->members();
        $loans = $this->loans();
        $data = $this->collect();

        return view('dashboard.index', [
            'records' => $records,
            'rows' => $rows,
            'lines' => $lines,
            'members' => $members,
            'loans' => $loans,
            'data' => $data,
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

    public function members()
    {
        $members = Members::all();
        return $members;
    }

    public function loans()
    {
        $loans = Registered_Loans::all();
        return $loans;
    }

    public function collect()
    {
        $data = Deposits::all();
        return $data;
    }
}
